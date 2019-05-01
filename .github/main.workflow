workflow "Check code stability" {
  resolves = ["Packaging"]
  on = "push"
}

action "Tests" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "clean test"
}

action "Packaging" {
  uses = "LucaFeger/action-maven-cli@765e218a50f02a12a7596dc9e7321fc385888a27"
  args = "package"
  needs = ["Tests"]
}
