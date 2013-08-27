This guide walks through the basics on administrating the mod-lang-scala repository.
Make sure you read the contribution guidelines in `CONTRIBUTE.md` before reading
this file.

# Source control - Git
mod-lang-scala uses git, hosted on GitHub, for version control. In this section,
we'll review the tasks and responsibilities of a mod-lang-scala repository
Project Admin.

## Project Admin
Project Admins have a very limited role. Only Project Admins are allowed to
push to upstream, and Project Admins never write any code directly on the
upstream repository. All Project Admins do is pull in and merge changes from
contributors (even if the "contributor" happens to be themselves) into
upstream, perform code reviews and either commit or reject such changes.

> All Contributors who are also Project Admins are encouraged to not merge
their own changes, to ensure that all changes are reviewed.

This approach ensures mod-lang-scala maintains quality on the main code source
tree, and allows for important code reviews to take place again ensuring
quality. Further, it ensures clean and easily traceable code history and makes
sure that more than one person knows about the changes being performed.

### Merging in patches

![Figure 2-1]
(https://docs.jboss.org/author/download/attachments/4784485/git_wf_3.png "Merging patch")

Patches submitted via JIRA are audited and promoted to the upstream repository
as detailed above. A Project Admin would typically create a working branch to
which the patch is applied and tested. The patch can be further modified,
cleaned up, and commit messages made clearer if necessary. The branch should
then be merged to the master or one of the maintenance branches before being
pushed.

More information on applying patches can be found in
[Chapter 5, Section 3 of Pro Git](http://progit.org/book/ch5-3.html), under
**Applying Patches From Email**.

### Handling pull requests

![Figure 2-2]
(https://docs.jboss.org/author/download/attachments/4784485/git_wf_4.png "Handling pull request")

Project Admins are also responsible for responding to pull requests. The
process is similar to applying a patch directly, except that when pulling in
changes from a forked repository, more than a single commit may be pulled in.
Again, this should be done on a newly created working branch, code reviewed,
tested and cleaned up as necessary.

If commits need to be altered - e.g., rebasing to squash or split commits, or
to alter commit messages - it is often better to contact the Contributor and
ask the Contributor to do so and re-issue the pull request, since doing so on
the upstream repo could cause update issues for contributors later on. If
commits were altered or three-way merge was performed during a merge instead
of fast-forward, it's also a good idea to check the log to make sure that the
resulting repository history looks OK:

    $ git log --pretty=oneline --graph --abbrev-commit  # History messed up due to a bad merge
    *   3005020 Merge branch 'ISPN-786' of git://github.com/Sanne/infinispan
    |\
    | * e757265 ISPN-786 Make dependency to log4j optional  <-- Same with cb4e5d6 - unnecessary
    * | cb4e5d6 ISPN-786 Make dependency to log4j optional  <-- Cherry-picked commit by other admin
    |/
    * ...

    $ git reset cb4e5d6  # revert the bad merge

It is therefore strongly recommended that you use the [handle_pull_request]
(https://github.com/maniksurtani/githelpers/blob/master/project_admins/handle_pull_request)
script that ensures a clean merge. If you still wish to do this manually,
please consider reading through the script first to get an idea of what needs
to happen.

More information on pulling changes from remote, forked repos can be found in
[Chapter 5, Section 3 of Pro Git](http://progit.org/book/ch5-3.html), under
**Checking Out Remote Branches**.

#### Possible trouble handling pull requests
If you have warnings about "Merge made by recursive" you have to fix it rebasing.
If you have warnings about "non-fast-forward" you have to rebase. If you see
"non-fast-forward updates were rejected" you shall never use "force" on upstream!
It means that another patch was merged before you and you have to update your
master again, and rebase again. "force" is allowed only in special maintenance
circumstances. If you find you're needing it to handle a pull request, then
you're doing it wrong, and the mistake might be a dangerous one! It's like the
good rule of never commit when you're drunk (coding is allowed).

> **Never use force on git push!** Using -f while pushing on a shared repository
such as upstream you could effectively erase other committed patches. Noone
shall ever use this option unless unanimously approved on the public mailing
list: the most dangerous aspect of it is that nobody gets any notification if
this happens, and we might think issues are solved but you silently removed
the fix and it's history from the repository.

### Cutting releases
Releases can only me cut by Project Admins... TBD.
