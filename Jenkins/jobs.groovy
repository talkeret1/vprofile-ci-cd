def pipelineScript = new File('/usr/share/jenkins/ref/Jenkinsfile').text

pipelineJob('vprofile-ci-cd') {
    description('CI/CD Pipeline for VProfile Application')

    definition {
        cps {
            script(pipelineScript)
            sandbox()
        }
    }
}