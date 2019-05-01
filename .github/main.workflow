workflow "Integration" {
  on = "push"
  resolves = ["List workspace"]
}

action "Compilation" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "clean package"
}

action "List workspace" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  needs = ["Compilation"]
  runs = "echo $MY_SECRET"
  secrets = ["GITHUB_TOKEN", "MY_SECRET"]
}
