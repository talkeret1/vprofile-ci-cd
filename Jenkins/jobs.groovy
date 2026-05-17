pipelineJob('vprofile-ci-cd') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/talkeret1/vprofile-ci-cd.git')
                    }
                    branches('main')
                }
            }
            scriptPath('Jenkins/Jenkinsfile')
        }
    }
}