#!/usr/bin/python

import re
import sys
import os
from multiprocessing import Process
from utils import *

try:
    from argparse import ArgumentParser
except:
    prettyprint('''
        Welcome to the Mod-Lang-Scala Release Script.
        This release script requires that you use at least Python 2.7.0.
        It appears that you do not have the collections.Counter available,
        which are available by default in Python 2.7.0.
        ''', Levels.FATAL)
    sys.exit(1)

modules = []
uploader = None
git = None
sbt_version_exprs = '\s*version             := ', '    version             := \"%s\",\n'
vertx_version_exprs = '\s*scala=', 'scala=io.vertx~lang-scala_2.10~%s:org.vertx.scala.platform.impl.ScalaVerticleFactory\n'


def help_and_exit():
    prettyprint('''
        Welcome to the Mod-Lang-Scala Release Script.
        
%s        Usage:%s
        
            $ bin/release.py <mod-lang-scala-version> <branch to tag from>
            
%s        E.g.,%s
        
            $ bin/release.py 1.0.0 %s<-- this will tag off master.%s
            $ bin/release.py 1.0.1 1.0.x %s<-- this will tag off 1.0.x.%s

    ''' % (
    Colors.yellow(), Colors.end_color(), Colors.yellow(), Colors.end_color(),
    Colors.green(), Colors.end_color()),
                Levels.INFO)
    sys.exit(0)


def validate_version(version):
    version_pattern = get_version_pattern()
    if version_pattern.match(version):
        return version.strip().upper()
    else:
        prettyprint("Invalid version '" + version + "'!\n", Levels.FATAL)
        help_and_exit()


def switch_to_tag_release(branch):
    if git.remote_branch_exists():
        git.switch_to_branch()
        git.create_tag_branch()
    else:
        prettyprint(
            "Branch %s cannot be found on upstream repository.  Aborting!"
            % branch, Levels.FATAL)
        sys.exit(100)


def update_version(base_dir, version, next):
    os.chdir(base_dir)
    files = [
        update_version_file(version, './project/VertxScalaBuild.scala', sbt_version_exprs),
        update_version_file(version, './src/test/resources/langs.properties',vertx_version_exprs)
    ]
    if not next:
        files.append(update_version_file(
            version, './README.asciidoc', vertx_version_exprs))
    # Now make sure this goes back into the repository.
    git.commit(files,
        "'Release Script: update mod-lang-scala version %s'" % version)

def update_version_file(version, file, exprs):
    f_in = open(file)
    f_out = open(file + '.tmp', 'w')
    re_version = re.compile(exprs[0])
    try:
        for l in f_in:
            if re_version.match(l):
                prettyprint("Update %s to version %s"
                            % (file, version), Levels.DEBUG)
                f_out.write(exprs[1] % version)
            else:
                f_out.write(l)
    finally:
        f_in.close()
        f_out.close()
    # Rename back file
    os.rename(file + ".tmp", file)
    return file

def do_task(target, args, async_processes):
    if settings.multi_threaded:
        async_processes.append(Process(target=target, args=args))
    else:
        target(*args)

### This is the starting place for this script.
def release():
    global settings
    global uploader
    global git
    assert_python_minimum_version(2, 5)

    parser = ArgumentParser()
    parser.add_argument('-d', '--dry-run', action='store_true', dest='dry_run',
                        help="release dry run", default=False)
    parser.add_argument('-v', '--verbose', action='store_true', dest='verbose',
                        help="verbose logging", default=True)
    parser.add_argument('-n', '--non-interactive', action='store_true',
                        dest='non_interactive',
                        help="non interactive script", default=False)
    parser.add_argument('-x', '--next-version', action='store', dest='next_version',
                        help="next mod-lang-scala version")

    # TODO Add branch...
    (settings, extras) = parser.parse_known_args()
    if len(extras) == 0:
        prettyprint("No release version given", Levels.FATAL)
        sys.exit(1)

    version = extras[0]
    interactive = not settings.non_interactive

    base_dir = os.getcwd()
    branch = "master"

    if len(sys.argv) > 2:
        branch = sys.argv[2]

    next_version = settings.next_version
    if next_version is None:
        proceed = input_with_default(
            'No next mod-lang-scala version given! Are you sure you want to proceed?', 'N')
        if not proceed.upper().startswith('Y'):
            prettyprint("... User Abort!", Levels.WARNING)
            sys.exit(1)

    prettyprint(
        "Releasing mod-lang-scala version %s from branch '%s'"
        % (version, branch), Levels.INFO)

    if interactive:
        sure = input_with_default("Are you sure you want to continue?", "N")
        if not sure.upper().startswith("Y"):
            prettyprint("... User Abort!", Levels.WARNING)
            sys.exit(1)

    prettyprint("OK, releasing! Please stand by ...", Levels.INFO)

    ## Set up network interactive tools
    if settings.dry_run:
        # Use stubs
        prettyprint(
            "*** This is a DRY RUN.  No changes will be committed.  Used to test this release script only. ***"
            , Levels.DEBUG)
        prettyprint("Your settings are %s" % settings, Levels.DEBUG)
        uploader = DryRunUploader()
    else:
        prettyprint("*** LIVE Run ***", Levels.DEBUG)
        prettyprint("Your settings are %s" % settings, Levels.DEBUG)
        uploader = Uploader(settings)

    git = Git(branch, version, settings)
    if interactive and not git.is_upstream_clone():
        proceed = input_with_default(
            'This is not a clone of an %supstream%s mod-lang-scala repository! Are you sure you want to proceed?' % (
            Colors.UNDERLINE, Colors.END), 'N')
        if not proceed.upper().startswith('Y'):
            prettyprint("... User Abort!", Levels.WARNING)
            sys.exit(1)

    ## Release order:
    # Step 1: Tag in Git
    prettyprint("Step 1: Tagging %s in git as %s" % (branch, version), Levels.INFO)
    switch_to_tag_release(branch)
    prettyprint("Step 1: Complete", Levels.INFO)

    # Step 2: Update version in tagged files
    prettyprint("Step 2: Updating version number", Levels.INFO)
    update_version(base_dir, version, False)
    prettyprint("Step 2: Complete", Levels.INFO)

    # Step 3: Build and test mod-lang-scala
    prettyprint("Step 3: Build and publish", Levels.INFO)
    build_publish(settings)
    prettyprint("Step 3: Complete", Levels.INFO)

    async_processes = []

    ## Wait for processes to finish
    for p in async_processes:
        p.start()

    for p in async_processes:
        p.join()

    ## Tag the release
    git.tag_for_release()

    if not settings.dry_run:
        # Update master with next version
        next_version = settings.next_version
        if next_version is not None:
            # Update to next version
            git.switch_to_branch() # switch to master
            prettyprint("Step 4: Updating version number for next release", Levels.INFO)
            update_version(base_dir, next_version, True)
            git.push_master_to_origin()
            prettyprint("Step 4: Complete", Levels.INFO)

        git.push_tags_to_origin()
        git.cleanup()
        git.push_master_to_origin()
    else:
        prettyprint(
            "In dry-run mode.  Not pushing tag to remote origin and not removing temp release branch %s." % git.working_branch
            , Levels.DEBUG)

if __name__ == "__main__":
    release()
