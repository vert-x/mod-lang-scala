This guide walks through the basics on contributing to mod-lang-scala repository.

# Pre-requisites
* Java 1.7: mod-lang-scala is baselined on Java 7.0, and is built and tested
using Sun Java 7.0.
* Git (optional): mod-lang-scala sources are stored in Git. If you don't
wish to install Git, you can download source bundle

# Scala
Since this is a Scala language implementation for Vert.x, the code base is
written in Scala.

# Building and Testing
Vert.x Scala uses SBT to build, test, package and publish itself. The key 
aspect that Vert.x Scala benefits from SBT is the fact that it has excellent
support for cross-compilation, allowing Vert.x Scala to build, test, package
and publish jars cross-compiled with multiple Scala versions. This is crucial
for Vert.x Scala users so that they get access to jars indepedent of the Scala
version that they use.

To get started with SBT, you need to [download SBT](http://www.scala-sbt.org/download.html), 
put it somewhere in your environment and add the `bin/sbt` to your path.
 
Here's a summary of the most commonly used commands to build Vert.x Scala:

* `sbt clean` - Cleans the project
* `sbt test` - Runs tests with the base Scala version
* `sbt package` - Creates jars and language extension module with the base Scala version
* `sbt publishM2` - Creates jars and language extension module with the base Scala version 
and publishes them to the local Maven repository. This is a useful command for
installing Vert.x with a locally built Vert.x Scala language extension.

As mentioned earlier, a crucial aspect of SBT is the ability to do the full 
build lifecycle with multiple Scala versions. This is very useful for example
to make sure that code change works fine with all Scala versions. Here's a 
brief guide to the useful cross-compilation commands:

* `sbt +test` - Runs tests with the base and cross-compiled Scala versions
* `sbt +publishM2` - Creates jars and language extension module with the both 
base and cross-compiled Scala versions and publishes them to the local Maven repository. 
This is a useful command for installing Vert.x with a locally built Vert.x 
Scala language extensions.
* `sbt +publishM2 +publish-signed` - Creates language extension modules in all
Scala versions and publishes them to the remote Maven repository where published 
Vert.x Scala jars are stored. Executing this command requires credentials from 
[Sonatype](http://www.sonatype.org/) and 
[some environment specific configuration](http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html).

# IDE integration
Any Vert.x Scala user wanting to contribute or simply browse the code benefits
from being able to load Vert.x Scala into an IDE. Here are some instructions 
depending on your IDE of choice:

## IntelliJ IDEA
The primary way of loading Vert.x Scala into IntelliJ is by installing the 
[SBT plugin](http://plugins.jetbrains.com/plugin/5007), restart Intellij, and 
then clicking `Open...` and selecting the folder where a Vert.x Scala clone 
lives. You can find more information about in the 
[Getting Started with SBT in IntelliJ IDEA](http://confluence.jetbrains.com/display/IntelliJIDEA/Getting+Started+with+SBT) 
guide.

If the primary method fails to create a project which you can compile and test,
there's a secondary method which involves using SBT command line to generate 
IntelliJ IDEA project files:

1. Go to [sbt-idea repository](https://github.com/mpeltonen/sbt-idea) and 
follow the installation instructions. It is recommended that the SBT plugin 
is installed in `~/.sbt/0.13/plugins/build.sbt` instead of in the Vert.x Scala 
project itself.
2. Go to root directory of the Vert.x Scala clone and execute: `sbt gen-idea`
3. Start IntelliJ IDEA and open the project selecting the root directory of 
the Vert.x Scala clone.

## Eclipse
To load Vert.x Scala into Eclipse, it's recommended that 
[Scala IDE](http://scala-ide.org/index.html) is installed and then follow the 
[Getting Started instructions](http://scala-ide.org/docs/user/gettingstarted.html)
which include instructions on how to import an SBT project.

Note that Vert.x Scala requires SBT `0.13.5` or higher, so when following the 
instructions above, you'll need the [sbteclipse](https://github.com/typesafehub/sbteclipse)
SBT plugin.

# License
mod-lang-scaa is licensed under the Apache License, Version 2.0 (the "License").
A license header must be placed at the top of each file containing this
information:

    Copyright ${year} the original author or authors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## Formatting
Escalante adheres to the [Scala style guide]
(http://docs.scala-lang.org/style/) with the addition of these rules:

* Preferably, stick to lines of 80 characters maximum, but if it needs to
be exceeded, please avoid lines greater than 100 columns, otherwise reading or
reviewing code in sites, such as GitHub, becomes a problem.
* Use short names for small scopes: `i`s, `j`s and `k`s are all but expected
in loops.
* Use longer names for larger scopes: External APIs should have longer and
explanatory names that confer meaning. `Future.collect` not `Future.all`.
* Avoid esoteric abbreviations, common ones are Ok. Everyone knows `ok`, `err`
or `defn` whereas `sfri` is not so common.
* Don't rebind names for different uses. Use `val`s.
* Avoid using overloading reserved names. Use active names for operations
with side effects `user.activate()` not `user.setActive()`
* Use descriptive names for methods that return values `src.isDefined` not
`src.defined`.
( Don't prefix getters with get. As per the previous rule, it's redundant:
`site.count` not `site.getCount`
* Use braces when importing several names from a package:
`import a.b.c.{d, e}`
* Use wildcards when more than six names are imported (Don't apply
this blindly: some packages export too many names). e.g.:
`import a._`
* When using collections, qualify names by importing
`scala.collection.immutable` and/or `scala.collection.mutable`. Mutable and
immutable collections have dual names. Qualifiying the names makes is obvious
to the reader which variant is being used (e.g. `immutable.Map`).
* Do not use relative imports from other packages
* Put imports at the top of the file. The reader can refer to all imports in
one place.

In the future, we expect to add [ScalaStyle](http://www.scalastyle.org/)
integration to our builds in order to detect formatting errors automatically.

# Issue Management
mod-lang-scala uses GitHub issues for issue management, hosted on
[https://github.com/vert-x/mod-lang-scala/issues]
(https://github.com/vert-x/mod-lang-scala/issues).
The kind of issues that should be reported there are:

* *Feature Request* if you want to request an enhancement or new feature for
mod-lang-scala
* *Bug* if you have discovered an issue
* *Task* if you wish to request a documentation, sample or process
(e.g. build system) enhancement or issue

# Source control - Git
mod-lang-scala uses git, hosted on GitHub, for version control. We recommend
Scott Chacon's excellent [Pro Git](http://progit.org/) as a valuable piece
of background reading. The book is released under the Creative Commons
license and can be downloaded in electronic form for free. At very least,
we recommend that [Chapter 2](http://progit.org/book/ch2-0.html), [Chapter 3]
(http://progit.org/book/ch3-0.html) and [Chapter 5]
(http://progit.org/book/ch5-0.html) of Pro Git are read before proceeding.

## Repositories
mod-lang-scala uses [http://github.com/vert-x/mod-lang-scala]
(https://github.com/vert-x/mod-lang-scala) as its canonical repository, and
this repository contains the stable code on master. Typically, only
*Project Admins* would be able to push to this repo while all else may clone
or fork this repo.

> As a convention, *upstream* is used as the name of the
[http://github.com/vert-x/mod-lang-scala](https://github.com/vert-x/mod-lang-scala)
repository. This repository is the canonical repository for mod-lang-scala. We
usually name origin the fork on github of each contributor. So the exact
meaning of origin is relative to the developer: you could think of origin as
your own fork.

## Roles
The project assumes one of 3 roles an individual may assume when interacting
with the mod-lang-scala codebase. The three roles here are:

* Project Admin (typically, no more than 3 or 4 people)
* Frequent Contributor
* Occasional Contributor

> None of the roles assume that you are a Red Hat or VMWare employee.
All it assumes is how much responsibility over the project has been granted to you.
Typically, someone may be in more than one role at any given time, and puts
on a different "hats" based on the task at hand.

This document focuses on Frequent and Occasional Contributors. If you want
to find out more about the the Project Admins, check this document.

### Occasional Contributor
This role defines a user who browses through the source code of the project
and occasionally submits patches. Patches may be submitted in one of two ways:

* Attach a patch file to the issue
* Creating a pull request on GitHub

The approach a contributor chooses to use depends entirely his/her personal
preference, but usually is tied to how the contributor accesses mod-lang-scala's
source code. If the contributor directly clones the upstream repository,
they should submit patch files. If the contributor instead uses their personal
GitHub account to fork the upstream repository, then they are should issue a
pull request.

> A GitHub pull request is the preferred method to submit a patch!

#### Attach a patch file to the JIRA issue

![Figure 1-1]
(https://docs.jboss.org/author/download/attachments/4784485/git_wf_1.png "Attach patch file workflow")

In this workflow, the contributor directly clones the upstream repository,
makes changes to his local clone, adequately tests and commits his work with
proper comments (more on commit comments later), and generates a patch.
The patch should then be attached to the issue.

To clone the upstream repository:

    $ git clone git@github.com:vert-x/mod-lang-scala.git

More information on generating patches suitable for attaching to a JIRA can be
found in [Chapter 5, Section 2 of Pro Git](http://progit.org/book/ch5-2.html),
under the section titled **Public Large Project**.

> Rather than posting the patches to the Vert.x Google Group, please attach
your patch to the issue or link it (i.e. Gist).

#### Creating a pull request on GitHub

![Figure 1-2]
(https://docs.jboss.org/author/download/attachments/4784485/git_wf_2.png "Create pull request workflow")

In this workflow, the contributor forks the mod-lang-scala upstream repository
on GitHub, clones their fork, and makes changes to this private fork. When
changes have been tested and are ready to be contributed back to the project,
a pull request is issued via GitHub so that one of the *Project Administrators*
can pull in the change.

To clone your upstream repository fork:

    $ git clone git@github.com:YOUR_GITHB_USERNAME/mod-lang-scala.git

> Topic Branches
It is desirable to work off a topic branch, even when using your own, forked
repository. A topic branch is created for every feature or bug fix you do.
Typically you would create one topic branch per issue, but if several patches
are related it's acceptable to have several commits in the same branch;
however different changes should always be identified by different commits.
Before you push your work onto your fork of the repository, it is often a good
idea to review your commits. Consolidating them (squashing) or breaking them
up as necessary and cleaning up commit messages should all be done while still
working off your local clone. Also, prior to issuing a pull request, you
should make sure you rebase your branch against the upstream branch you
expect it to be merged into.  Also, only submit pull requests for your
branch - not for your master!

The section on **Public Small Project** in [Chapter 5, Section 2 of Pro Git]
(http://progit.org/book/ch5-2.html) has more information on this style of
workflow.

##### A worked example
1. Make sure your master is synced up with upstream. See [this section]
(#keeping-your-repo-in-sync-with-upstream) for how to do this.
2. Create new branch for your topic and switch to it. For the example issue,
#12345:

        git checkout -b 12345 master

3. Do your work. Test. Repeat
4. Commit your work on your topic branch
5. Push your topic branch to GitHub. For example:

        git push origin 12345

6. Issue a pull request using the GitHub pull request system
7. Once your pull request has been applied upstream, delete the topic branch
both locally and on your fork. For example:

        git branch -d 12345 && git push origin :12345

8. Sync with upstream again so that your changes now appear in your master branch

If your topic branch has been open for a while and you are afraid changes
upstream may clash with your changes, it makes sense to rebase your topic
branch before you issue a pull request. To do this:

1. Sync your master branch with upstream

        git checkout master
        git pull upstream master

2. Switch to your topic branch. For example:

        git checkout 12345

3. Rebase your topic branch against master:

        git rebase master

4. During the rebase process you might need to fix conflicts;
5. When you're done test your code again.
6. Push your rebased topic branch to your repo on GitHub (you will likely
need to force this with the -f option).

        git push -f origin 12345

7. Continue your work on your topic branch.

> If you are sharing your forked mod-lang-scala repo with others,
then do not rebase! Use a merge instead.

#### Multi-step coordination between developers using forked repositories

Sometimes a feature/task is rather complex to implement and requires
competence from multiple areas of the projects. In such occasions it is not
uncommon for developers to coordinate feature implementation using personal
forks of mod-lang-scala prior to finally issuing request to integrate into
mod-lang-scala main repository on GitHub.

For example, developer A using his personal mod-lang-scala fork creates a
topic branch T and completes as much work as he/she can before requesting for
assistance from developer B. Developer A pushes topic T to his personal
mod-lang-scala fork where developer B picks it up and brings it down to his
local repo. Developer B then in turn completes necessary work, commits his/her
changes on branch T, and finally pushes back T to his own personal fork.
After issuing request for pull to developer A, developer B waits for
notification that developer A integrated his changes. This exchange can be
repeated as much as it is necessary and can involve multiple developers.

##### A worked example
This example assumes that developer A and B have added each others
mod-lang-scala forked repositories with the git add remote command.

1. For example, developer B would add developer A's personal mod-lang-scala
fork repository with the command:

        git remote add devA https://github.com/developerA/mod-lang-scala.git

2. Developer A starts implementing feature #244 and works on a local topic
branch `244`. Developer A pushes `244` to personal mod-lang-scala fork.
For example:

        git push origin 244

3. Developer B fetches branch `244` to local repository. For example:

        git fetch devA 244:my_244

4. Developer B works on local branch `my_244`
5. Developer B commits changes, pushes `my_244` to own fork.

        git push origin my_244

6. Developer B sends pull request to developer A to integrate changes
from `my_244` to `244`

### Frequent Contributor

A frequent contributor will only ever submit patches via a pull requests.
The pull request will be submitted via GitHub.

Frequent contributors should always fork the upstream project on GitHub and
work off a clone of this fork. This is very similar to Creating a pull
request on GitHub workflow used by a *Occasional Contributor*.

## Keeping your repo in sync with upstream

### If you have cloned upstream

If you have a clone of the upstream, you may want to update it from time to
time. Running:

    $ git fetch origin
    $ git fetch origin --tags

will often do the trick. You could then pull the specific branches you would
need to update:

    $ git checkout master
    $ git pull origin master
    $ git checkout 0.x
    $ git pull origin 0.x

#### Updating topic branches
You should rebase your topic branches at this point so that they are
up-to-date and when pulled by upstream, upstream can fast-forward the release
branches:

    $ git checkout <topic>_master
    $ git rebase master

and/or

    $ git checkout <topic>_0.x
    $ git rebase 0.x

### If you have forked upstream
If you have a fork of upstream, you should probably define upstream as one of
your remotes:

    $ git remote add upstream git@github.com:vert-x/mod-lang-scala.git

You should now be able to fetch and pull changes from upstream into your local
repository, though you should make sure you have no uncommitted changes.
(You do use topic branches, right?)

    $ git fetch upstream
    $ git fetch upstream --tags
    $ git checkout master
    $ git pull upstream master
    $ git push origin master
    $ git checkout 0.x
    $ git pull upstream 0.x
    $ git push origin 0.x

A script can do this for you - have a look at [sync_with_upstream]
(https://github.com/maniksurtani/githelpers/blob/master/contributors/sync_with_upstream).

#### Updating topic branches
Again, you should rebase your topic branches at this point so that they are
up-to-date and when pulled by upstream, upstream can fast-forward the release
branches:

    $ git checkout <topic>_master
    $ git rebase master

and/or

    $ git checkout <topic>_0.x
    $ git rebase 0.x

The [sync_with_upstream](https://github.com/maniksurtani/githelpers/blob/master/contributors/sync_with_upstream)
script can do this for you if your topic branch naming conventions match the script.

## Tips on enhancing git

TODO

# Committing your work

## Release branches
mod-lang-scala has one main release branch, *master*. Work should never be
committed directly to any of these release branches; topic branches should
always be used for work, and these topic branches should be merged in.

## Topic branches
Some of the biggest features of git are speed and efficiency of branching,
and accuracy of merging. As a result, best practices involve making frequent
use of branches. Creating several topic branches a day, even, should not be
considered excessive, and working on several topic branches simultaneously
again should be commonplace.

[Chapter 3, Section 4 of Pro Git](http://progit.org/book/ch3-4.html) has a
detailed discussion of topic branches. For mod-lang-scala, it makes sense to
create a topic branch and name it after the issue number it corresponds to.
(if it doesn't correspond to a GitHub issue, a simple but descriptive name
should be used).

## Comments
It is extremely important that comments for each commit are clear and follow
certain conventions. This allows for proper parsing of logs by git tools.
Read [this article](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
on how to format comments for git and adhere to them. Further to the
recommendations in the article, the short summary of the commit message
should be in the following format:

    Issue-#XXX Subject line of the issue in question

This can optionally be followed by a detailed explanation of the commit.
Why it was done, how much of it was completed, etc. You may wish to express
this as a list, for example:

    * Add a unit test
    * Add more unit tests
    * Fix regressions
    * Solve major NP-Complete problems

Make sure however to split separate concerns - especially if they are
unrelated - in separate commits.

It might happen that a single commit fixes multiple issues. This shouldn't be
the norm, but if it happens, it's recommended that all issue ids are specified
in the title of commit, followed by a more detailed explanation following the
example above e.g.

    Issue-#XXX Issue-#YYYY Summary of issues

    * Fix Issue-#XXXX by doing...
    * Fix Issue-#YYYY by doing...

## Commits
Sometimes work on your topic branch may include several commits.
For example, committing a test. Then committing another test.
Then perhaps committing a fix. And perhaps fixing your own fix in the
next commit... Before issuing a pull request for this topic branch,
consider cleaning up these commits. Interactive rebasing helps you squash
several commits into a single commit, which is often more coherent to deal
with for others merging in your work. [Chapter 6, Section 4 of Pro Git]
(http://progit.org/book/ch6-4.html) has details on how to squash commits
and generally, clean up a series of commits before sharing this work
with others. Note that you can also easily reorder them, just change the
order of lines during the interactive rebase process.

Also, it is important to make sure you don't accidentally commit files
for which no real changes have happened, but rather, whitespace has been
modified. This often happens with some IDEs. git diff --check should be run
before you issue such a pull request, which will check for such "noise"
commits and warn you accordingly. Such files should be reverted and not be
committed to the branch.

Adhering to mod-lang-scala's code style guidelines will help minimise
"noise" commits. *Project Admins* are going to ask contributors to
reformat their code if necessary.
