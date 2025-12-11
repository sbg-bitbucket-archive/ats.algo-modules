library(
    identifier: "common-ci@master",
    retriever: modernSCM(
        [
            $class: 'GitSCMSource',
            remote: 'ssh://git@stash.skybet.net:7999/ats/common-ci.git'
        ]
    )
)

def projectName = 'algo-modules'

pipeline {
    agent none

    options {
        timestamps()
        skipDefaultCheckout()
    }

    triggers {
        cron(atsRepos.syncJobCron)
    }

    stages {
        stage('Execute Downstream Pipeline') {
            steps {
                build job: atsRepos.syncJobName, parameters: [[$class: 'StringParameterValue', name: 'PROJECT_NAME', value: projectName]]
            }
        }
    }
}
