import jenkins.model.*

def seedJobName = "Seed Job"
def seedJob = Jenkins.instance.getItem(seedJobName)
if (seedJob) {
    println("Found seed job: ${seedJobName}. Scheduling a build...")
    seedJob.scheduleBuild2(0)
} else {
    println("Seed job: ${seedJobName} not found. Please check the configuration.")
}
