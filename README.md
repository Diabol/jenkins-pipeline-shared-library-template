Jenkins Pipeline Shared Library Template
========================================

This project is intended for use with [Jenkins](https://jenkins.io/) and Global Pipeline Libraries through the 
[Pipeline Shared Groovy Libraries Plugin](https://wiki.jenkins.io/display/JENKINS/Pipeline+Shared+Groovy+Libraries+Plugin).

A common scenario when developing Jenkins [declarative pipelines](https://jenkins.io/doc/book/pipeline/syntax/), is
to bundle common custom pipeline tasks in a shared library so that all Jenkins pipeline configurations in an organisation
can leverage from them without the need to reimplement the same logic.

This project provides a project template for developing shared Jenkins pipeline libraries as specified in the Jenkins
[documentation](https://jenkins.io/doc/book/pipeline/shared-libraries/). The project is setup using Gradle which enables you to develop and unit
test your custom Jenkins pipeline library code.

Requirements
---
[Apache Groovy](http://groovy-lang.org/)

Install
---
    git clone https://github.com/Diabol/jenkins-pipeline-shared-library-template.git
    cd jenkins-pipeline-shared-library-template
    ./gradlew build test

Install the shared library as described in the Jenkins [shared library documentation](https://jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries).

Structure
---
The project contains an example pipeline method _maintainer_, which allows you to output the project maintainer in the console log.
This is only used as an example. Adapt and add new classes according to your needs. 

    ├── src                       (your source code classes goes here)
    │   └── se.diabol.jenkins.pipeline.lib
    │       └── Constants.groovy  (example Groovy class)
    ├── test                      (your unit test classes goes here)
    │   └── MaintainerTest.groovy (example unit test class)
    └── vars                      (your shared library classes goes here)
        └── maintainer.groovy     (logic for your custom method - filename to match Jenkins pipeline step name)

Example usage in a Jenkins declarative pipeline:
```
/**
 * Library name should match the name configured in Jenkins > Configure system > Global Pipeline Libraries.
 * Annotation can be omitted if configured to be loaded implicitly.
 */
@Library('jenkins-pipeline-shared-library-template') _
pipeline {
    agent any
    stages {
        stage('Commit stage') {
            steps {
                maintainer 'Diabol AB'
            }
        }
    }
}
```
Pipeline console output:
```
Started by user anonymous
Loading library jenkins-pipeline-shared-library-template@master
...
[Pipeline] node
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Commit stage)
[Pipeline] echo
Project maintained by Diabol AB
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS

```
Considerations
----
For many use cases there is a benefit in providing a custom and simplified DSL to create Jenkins pipelines, instead of
requiring repetitive pipeline configurations for each project. So instead of each project specifying a full configuration
such as in the example above, the pipeline itself can be extracted to a pipeline method (residing in the _vars_ directory).

Example:
```vars/continuousDeliveryPipeline.groovy```

```
#!groovy

def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline {
        agent any
        stages {
            stage('Commit stage') {
                steps {
                    maintainer config.maintainer
                }
            }
        }
    }
}
```
... which allows your pipeline configuration (e.g. in a Jenkinsfile) to look like:
```
continuousDeliveryPipeline {
    maintainer = 'Diabol AB'
}
```
with a convenient DSL with less need for repetition in your pipeline configurations.


Configuration
----
The library name used in the pipeline script must match what is configured as the library name in Jenkins > Configure system > Global Pipeline Libraries.
See this blog post on how to configure shared groovy libraries programmatically: http://blog.diabol.se/?p=1052

Contact and feedback
----
Feel free to open an issue or pull request if you find areas of improvement.

Maintained by [Diabol AB](https://github.com/Diabol/)

Happy pipelining!
