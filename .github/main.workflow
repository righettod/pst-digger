workflow "Integration" {
  resolves = ["List workspace"]
  on = "push"
}

action "Compilation" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "clean package"
}

action "List workspace" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  needs = ["Compilation"]
  runs = "printenv"
  secrets = ["MY_SECRET"]
}
