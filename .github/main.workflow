workflow "Integration" {
  resolves = [
    "List workspace",
    "Notify start",
  ]
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

action "Notify start" {
  uses = "swinton/httpie.action@8ab0a0e926d091e0444fcacd5eb679d2e2d4ab3d"
  args = ["POST", "https://enini83jvxr5e.x.pipedream.net/", "ping=YES"]
}
