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


def help_and_exit():
    prettyprint('''
        Welcome to the Mod-Lang-Scala Release Script.
        
%s        Usage:%s
        
            $ bin/release.py <mod-lang-scala-version>
            
%s        E.g.,%s
        
            $ bin/release.py 0.1.0 %s<-- this will tag off master.%s
            
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


def update_version(base_dir, version):
    os.chdir(base_dir)
    gradle_props = './gradle.properties'

    pieces = re.compile('[\.\-]').split(version)

    # 1. Update mod-lang-scala version in root gradle properties file
    f_in = open(gradle_props)
    f_out = open(gradle_props + '.tmp', 'w')
    re_version = re.compile('\s*version=')
    try:
        for l in f_in:
            if re_version.match(l):
                prettyprint("Update %s to version %s"
                            % (gradle_props, version), Levels.DEBUG)
                f_out.write('version=%s\n' % version)
            else:
                f_out.write(l)
    finally:
        f_in.close()
        f_out.close()

    # Rename back gradle properties file
    os.rename(gradle_props + ".tmp", gradle_props)

    # Now make sure this goes back into the repository.
    git.commit([gradle_props],
        "'Release Script: update mod-lang-scala version %s'" % version)

    # And return the next version - currently unused
    return pieces[0] + '.' + str(int(pieces[1]) + 1) + '.' + '0-SNAPSHOT'


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

#    next_version = settings.next_version
#    if next_version is None:
#        proceed = input_with_default(
#        'No next mod-lang-scala version given! Are you sure you want to proceed?', 'N')
#        if not proceed.upper().startswith('Y'):
#            prettyprint("... User Abort!", Levels.WARNING)
#            sys.exit(1)

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
    update_version(base_dir, version)
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
        git.push_tags_to_origin()
        git.cleanup()
        git.push_master_to_origin()
    else:
        prettyprint(
            "In dry-run mode.  Not pushing tag to remote origin and not removing temp release branch %s." % git.working_branch
            , Levels.DEBUG)

#        # Update master with next version
#        next_version = settings.next_version
#        if next_version is not None:
#            # Update to next version
#            prettyprint("Step 4: Updating version number for next release", Levels.INFO)
#            update_version(base_dir, next_version)
#            git.push_master_to_origin()
#            prettyprint("Step 4: Complete", Levels.INFO)


if __name__ == "__main__":
    release()
