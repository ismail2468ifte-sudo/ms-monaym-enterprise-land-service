pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

// =========================================================================
// Google Play Store Policy & AAB Bundle Non-Governmental Declaration:
// এই এ্যাপটি একটি বেসরকারি এ্যাপ,শুধু এ্যাপের মধ্যে থাকা কিছু কাজের সুবিধার জন্য সরকারি URL লিংক সোর্স ব্যবহার করা হবে।।
// This is a private, non-governmental application. Official government URL link
// sources (land.gov.bd, eporcha.gov.bd) are utilized solely for facilitating land services.
// =========================================================================

rootProject.name = "M.S MONAYM ENTERPRISE"

include(":app")
