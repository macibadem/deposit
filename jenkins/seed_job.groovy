job('Seed Job') {
    scm {
        git {
            remote {
                url('${LOCAL_REPO_PATH}')
            }
            branch('master')
        }
    }
    steps {
        dsl {
            scriptText("""
          pipelineJob('Deposit Pipeline') {
            definition {
              cpsScm {
                scm {
                  git {
                    remote {
                      url('${LOCAL_REPO_PATH}')
                    }
                    branch('master')
                  }
                }
                scriptPath('Jenkinsfile')
              }
            }
          }
      """)
        }
    }
}
