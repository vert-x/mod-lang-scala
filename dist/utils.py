# -*- coding: utf-8; -*-
#
# Copyright 2012 Red Hat, Inc. and/or its affiliates.
#
# Licensed under the Eclipse Public License version 1.0, available at
# http://www.eclipse.org/legal/epl-v10.html

import os
import fnmatch
import re
import subprocess
import sys
import readline
import shutil
import random
#settings_file = '%s/.mod-lang-scala' % os.getenv('HOME')

### Known config keys
maven_pom_xml_namespace = "http://maven.apache.org/POM/4.0.0"
#default_settings = {'dry_run': False, 'multi_threaded': False, 'verbose': False, 'use_colors': True}
#boolean_keys = ['dry_run', 'multi_threaded', 'verbose']

class Colors(object):
    MAGENTA = '\033[95m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    RED = '\033[91m'
    CYAN = '\033[96m'
    END = '\033[0m'
    UNDERLINE = '\033[4m'

    @staticmethod
    def magenta():
        if use_colors():
            return Colors.MAGENTA
        else:
            return ""

    @staticmethod
    def green():
        if use_colors():
            return Colors.GREEN
        else:
            return ""

    @staticmethod
    def yellow():
        if use_colors():
            return Colors.YELLOW
        else:
            return ""

    @staticmethod
    def red():
        if use_colors():
            return Colors.RED
        else:
            return ""

    @staticmethod
    def cyan():
        if use_colors():
            return Colors.CYAN
        else:
            return ""

    @staticmethod
    def end_color():
        if use_colors():
            return Colors.END
        else:
            return ""


class Levels(Colors):
    C_DEBUG = Colors.CYAN
    C_INFO = Colors.GREEN
    C_WARNING = Colors.YELLOW
    C_FATAL = Colors.RED
    C_ENDC = Colors.END

    DEBUG = "DEBUG"
    INFO = "INFO"
    WARNING = "WARNING"
    FATAL = "FATAL"

    @staticmethod
    def get_color(level):
        if use_colors():
            return getattr(Levels, "C_" + level)
        else:
            return ""


def use_colors():
    return True

#  return ('use_colors' in settings and settings['use_colors']) or ('use_colors' not in settings)

def prettyprint(message, level):
    start_color = Levels.get_color(level)
    end_color = Levels.end_color()

    print "[%s%s%s] %s" % (start_color, level, end_color, message)


def to_bool(x):
    if type(x) == bool:
        return x
    if type(x) == str:
        return {'true': True, 'false': False}.get(x.strip().lower())


def input_with_default(msg, default):
    i = raw_input(
        "%s %s[%s]%s: " % (msg, Colors.magenta(), default, Colors.end_color()))
    if i.strip() == "":
        i = default
    return i


def get_search_path(executable):
    """Retrieves a search path based on where the current executable is located.  Returns a string to be prepended to add"""
    in_bin_dir = re.compile('^.*/?bin/.*.py')
    if in_bin_dir.search(executable):
        return "./"
    else:
        return "../"


def strip_leading_dots(filename):
    return filename.strip('/. ')


def to_set(list):
    """Crappy implementation of creating a Set from a List.  To cope with older Python versions"""
    temp_dict = {}
    for entry in list:
        temp_dict[entry] = "dummy"
    return temp_dict.keys()


class GlobDirectoryWalker:
    """A forward iterator that traverses a directory tree"""

    def __init__(self, directory, pattern="*"):
        self.stack = [directory]
        self.pattern = pattern
        self.files = []
        self.index = 0

    def __getitem__(self, index):
        while True:
            try:
                file = self.files[self.index]
                self.index = self.index + 1
            except IndexError:
                # pop next directory from stack
                self.directory = self.stack.pop()
                self.files = os.listdir(self.directory)
                self.index = 0
            else:
                # got a filename
                fullname = os.path.join(self.directory, file)
                if os.path.isdir(fullname) and not os.path.islink(fullname):
                    self.stack.append(fullname)
                if fnmatch.fnmatch(file, self.pattern):
                    return fullname


class Git(object):
    '''Encapsulates git functionality necessary for releasing Infinispan'''
    cmd = 'git'
    # Helper functions to clean up branch lists
    @staticmethod
    def clean(e): return e.strip().replace(' ', '').replace('*', '')

    @staticmethod
    def non_empty(e): return e != None and e.strip() != ''

    @staticmethod
    def current(e): return e != None and e.strip().replace(' ', '').startswith(
        '*')

    def __init__(self, branch, tag_name, settings):
        self.settings = settings
        if not self.is_git_directory():
            raise Exception(
                'Attempting to run git outside of a repository. Current directory is %s' % os.path.abspath(
                    os.path.curdir))

        self.branch = branch
        self.tag = tag_name
        self.verbose = False
        if settings.verbose:
            self.verbose = True
        rand = '%x'.upper() % (random.random() * 100000)
        self.working_branch = '__temp_%s' % rand
        self.original_branch = self.current_branch()

    def run_git(self, opts):
        call = [self.cmd]
        if type(opts) == list:
            for o in opts:
                call.append(o)
        elif type(opts) == str:
            for o in opts.split(' '):
                if o != '':
                    call.append(o)
        else:
            raise Error("Cannot handle argument of type %s" % type(opts))
        if self.settings.verbose:
            prettyprint('Executing %s' % call, Levels.DEBUG)
        # Non-interactive shells have issues with piping, so do normal check calls
        if self.settings.non_interactive:
            return subprocess.check_call(call)
        else:
            return subprocess.Popen(
                call, stdout=subprocess.PIPE).communicate()[0].split('\n')

    def is_git_directory(self):
        if self.settings.non_interactive:
            return True
        else:
            return self.run_git('branch')[0] != ''

    def is_upstream_clone(self):
        if self.settings.non_interactive:
            return True
        else:
            r = self.run_git('remote show -n origin')
            cleaned = map(self.clean, r)

            def push(e): return e.startswith('PushURL:')

            def remove_noise(e): return e.replace('PushURL:', '')

            push_urls = map(remove_noise, filter(push, cleaned))
            return len(push_urls) == 1 and \
                   push_urls[0] == 'git@github.com:vert-x/mod-lang-scala.git'

    def clean_branches(self, raw_branch_list):
        return map(self.clean, filter(self.non_empty, raw_branch_list))

    def remote_branch_exists(self):
        """Tests whether the branch exists on the remote origin"""
        if self.settings.non_interactive:
            return True
        else:
            branches = self.clean_branches(self.run_git("branch -r"))

            def replace_origin(b): return b.replace('origin/', '')

            return self.branch in map(replace_origin, branches)

    def switch_to_branch(self):
        """Switches the local repository to the specified branch.  Creates it if it doesn't already exist."""
        if self.settings.non_interactive:
            # If non-interactive, the repo is clean, checkout directly, unless master
            # TODO: Uncomment this and make it conditional when non-master
            # self.run_git("branch %s origin/%s" % (self.branch, self.branch))
            self.run_git("checkout %s" % self.branch)
        else:
            local_branches = self.clean_branches(self.run_git("branch"))
            if self.branch not in local_branches:
                self.run_git("branch %s origin/%s" % (self.branch, self.branch))
            self.run_git("checkout %s" % self.branch)

    def create_tag_branch(self):
        '''Creates and switches to a temp tagging branch, based off the release branch.'''
        self.run_git("checkout -b %s %s" % (self.working_branch, self.branch))

    def commit(self, files, message):
        '''Commits the set of files to the current branch with a generated commit message.'''
        for f in files:
            self.run_git("add %s" % f)

        self.run_git(["commit", "-m", message])

    def tag_for_release(self):
        """Tags the current branch for release using the tag name."""
        self.run_git(["tag", "-a", "-m", "'Release Script: tag %s'" % self.tag,
                      self.tag])

    def push_tags_to_origin(self):
        '''Pushes the updated tags to origin'''
        self.run_git("push origin --tags")

    def push_master_to_origin(self):
        '''Pushes the updated tags to origin'''
        self.run_git("push origin master")

    def current_branch(self):
        """Returns the current branch you are on"""
        if (self.settings.non_interactive):
            return 'master'
        else:
            return map(self.clean, filter(self.current, self.run_git('branch')))[0]

    def cleanup(self):
        '''Cleans up any temporary branches created'''
        self.run_git("checkout %s" % self.original_branch)
        self.run_git("branch -D %s" % self.working_branch)


class DryRun(object):
    location_root = "%s/%s" % (os.getenv("HOME"), "mod-lang-scala-release-dry-run")

    def find_version(self, url):
        return os.path.split(url)[1]

    def copy(self, src, dst):
        prettyprint(
            "  DryRun: Executing %s" % ['rsync', '-rv', '--protocol=28', src,
                                        dst], Levels.DEBUG)
        try:
            os.makedirs(dst)
        except:
            pass
        subprocess.check_call(['rsync', '-rv', '--protocol=28', src, dst])


class Uploader(object):
    def __init__(self, settings):
        if settings.verbose:
            self.scp_cmd = ['scp', '-rv']
            self.rsync_cmd = ['rsync', '-rv', '--protocol=28']
        else:
            self.scp_cmd = ['scp', '-r']
            self.rsync_cmd = ['rsync', '-r', '--protocol=28']

    def upload_scp(self, fr, to, flags=[]):
        self.upload(fr, to, flags, list(self.scp_cmd))

    def upload_rsync(self, fr, to, flags=[]):
        self.upload(fr, to, flags, list(self.rsync_cmd))

    def upload(self, fr, to, flags, cmd):
        for e in flags:
            cmd.append(e)
        cmd.append(fr)
        cmd.append(to)
        subprocess.check_call(cmd)


class DryRunUploader(DryRun):
    def upload_scp(self, fr, to, flags=[]):
        self.upload(fr, to, "scp")

    def upload_rsync(self, fr, to, flags=[]):
        self.upload(fr, to.replace(':', '____').replace('@', "__"), "rsync")

    def upload(self, fr, to, type):
        self.copy(fr, "%s/%s/%s" % (self.location_root, type, to))


def build_publish(settings):
    """Builds the distribution in the current working dir"""
    sbt_commands = [['clean', "install"]]
    if not settings.dry_run:
        sbt_commands.append(['uploadArchives'])

    for c in sbt_commands:
        c.insert(0, './gradlew')
        prettyprint("Execute Gradle command %s" % c, Levels.DEBUG)
        subprocess.check_call(c)


def get_version_pattern():
    return re.compile("^([0-9]\.[0-9])\.[0-9]", re.IGNORECASE)


def get_version_major_minor(full_version):
    pattern = get_version_pattern()
    matcher = pattern.match(full_version)
    return matcher.group(1)


def assert_python_minimum_version(major, minor):
    e = re.compile('([0-9])\.([0-9])\.([0-9]).*')
    m = e.match(sys.version)
    major_ok = int(m.group(1)) == major
    minor_ok = int(m.group(2)) >= minor
    if not (minor_ok and major_ok):
        prettyprint("This script requires Python >= %s.%s.0.  You have %s" % (
        major, minor, sys.version), Levels.FATAL)
        sys.exit(3)

