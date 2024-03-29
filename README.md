# BiteByByte

> # Warning 
> When you clone this project, **DO NOT** use the oneDrive folder. You will get terrible bugs and errors.

## Conventional Commits

Each commit that you make should be error-free and compile. With an interactive rebase you can squash commits, drop them, rewrite them and reorder them. So don't be afraid to make errors as long as you keep it located in your branch.

We will follow the [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/) format:

    type(scope): Subject line

    Body

Where `type` can be:

|type|Usage|
|-|-|
|feat|When adding new functional code|
|test|When you're writing tests|
|refactor|For improving existing code|
|style|Nothing has changed, except the formatting|
|docs|When writing documentation (readme, doc comments)|
|fix|For fixing bugs|
|build|For changing project configuration (version number, including libraries)|
|wip|Work in progress, you need to change this commit later to one of the other types|

`Scope` indicates which part of the codebase you change. Examples are: Camera, FireBase, UI, etc.

The `Subject line`:

+ should start with a capital letter
+ be written in the imperative form 
    + "Fix infinite loop bug" 

## GitHub workflow

### Branching Strategy

We are using a light version of GitFlow. This mean that we have the following branches:

+ **main** - Should only contain code that we think would be ready to publish
+ **develop** - Contains the whole history of the project. Should always compile.
+ **feature-branches** - Short-lived branches which represent a feature of the app

Here are the rules of this workflow:

1. Feature branches are always branched from develop
2. Feature branches are linked to GitHub issues
3. Main will not be merged into any other branch
4. Feature branches will always merge into develop, not main
5. Develop will merge into main if the code is ready to be published


### Workflow

A typical workflow on feature A would look like this:

1. Setup the feature branch
2. Work on the feature branch
3. Request a pull review to merge on develop

#### Setup the feature branch

First, you create a GitHub Issue with the feature you want to implement. Then on the right side, click on the `create branch` in the development section. After that, Assign yourself to that issue.

Then you need to get that branch locally.

    git fetch
    git switch [name of feature branch]

#### Work on the feature branch

Before you begin working, you should first make sure that your branches are up to date.

    git switch develop
    git pull

    git switch [name of feature branch]
    git rebase develop

If there are no conflicts, you can begin with coding. Note that this make your origin branch out of sync. To get this up to date, **DO NOT** pull with force, but use `git push --force-with-lease`. This command gets the origin branch up to date and will not overwrite new commands on the branch.

You should regularly commit to your feature branch

    Long commit message: git commit
    Short commit message: git commit -m [message]
    2 line commit message: git commit -m [first line] -m [second line]

Feel free to clean up your commits inbetween

    git rebase -i develop

For heavy cleanups, I strongly recommend to create a backup branch, so that you don't accidentally delete all your progress.

    git branch backup

When you're done with coding, you should push your code to GitHub.

    git push

#### Request a pull review

First, make sure that GitHub has the **latest changes** of your feature branch.

    git switch [name of feature branch]
    git push

Then in GitHub, **request a pull review** to merge your code into develop. You do this by selecting your feature branch and clicking the green "open pull request" button in the "contribute" dropdown".

Make sure you are merging **from** your feature branch **into** the develop branch.

When you open the pull request, you can automatically see if there are any conflicts.

While the pull request is open, you can make any changes locally to the branch, push it and the pull request will **update automatically**. 

After your pull review is approved, there are multiple ways to merge it.

1. Via the GitHub interface
2. Doing the merge locally and pushing it

**GithubInterface**

Below, there is a dropdown menu with the text "Create a merge commit". Select the option "rebase and merge".

If all went well, delete your future branch locally and on GitHub.

**Merge locally**

> If you go this route, make sure you know what you're doing. Or ask Tristan or Alies to help you.

First, make sure that the develop branch is up to date.

    git switch develop
    git pull

Then, rebase your feature branch onto develop

    git switch [name of feature branch]
    git rebase develop -i

If you add the `-i` flag, then you can clean up the branch one last time

Then we will do a fast-forward merge in develop

    git switch develop
    git merge --ff-only [name of feature branch]

Then push your changes to GitHub

    git push

If all went well, you can delete the branches

    git branch -d [name of feature branch]

When you do this manually, the pull request will automatically detect that it got merged, so you can close it.








