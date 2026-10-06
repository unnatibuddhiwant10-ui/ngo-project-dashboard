#!/usr/bin/env python3
"""
Ansible Idempotency & Rollback Demonstration Harness
Simulates Ansible playbook execution, idempotency verification (changed=0),
health check verification, and automated rollback.
"""

import time
import sys

def print_banner(text):
    print("=" * 70)
    print(text.center(70))
    print("=" * 70)

def simulate_playbook_run(run_number):
    print_banner(f"PLAY [Deploy NGO Project Dashboard Service] - RUN #{run_number}")
    print("\nTASK [Gathering Facts] *************************************************")
    time.sleep(0.3)
    print("ok: [localhost]")

    print("\nTASK [1. Ensure dedicated application group exists] *********************")
    time.sleep(0.3)
    if run_number == 1:
        print("changed: [localhost] => {\"changed\": true, \"gid\": 1001, \"name\": \"ngogroup\"}")
    else:
        print("ok: [localhost] => {\"changed\": false, \"gid\": 1001, \"name\": \"ngogroup\"}")

    print("\nTASK [2. Ensure non-root service user exists] ***************************")
    time.sleep(0.3)
    if run_number == 1:
        print("changed: [localhost] => {\"changed\": true, \"name\": \"ngoapp\", \"uid\": 1001}")
    else:
        print("ok: [localhost] => {\"changed\": false, \"name\": \"ngoapp\", \"uid\": 1001}")

    print("\nTASK [3. Ensure required directories exist with correct permissions] ****")
    time.sleep(0.3)
    if run_number == 1:
        print("changed: [localhost] => (item=/opt/ngo-dashboard)")
        print("changed: [localhost] => (item=/opt/ngo-dashboard/logs)")
        print("changed: [localhost] => (item=/opt/ngo-dashboard/config)")
        print("changed: [localhost] => (item=/opt/ngo-dashboard/releases)")
    else:
        print("ok: [localhost] => (item=/opt/ngo-dashboard)")
        print("ok: [localhost] => (item=/opt/ngo-dashboard/logs)")
        print("ok: [localhost] => (item=/opt/ngo-dashboard/config)")
        print("ok: [localhost] => (item=/opt/ngo-dashboard/releases)")

    print("\nTASK [4. Ensure system prerequisites are installed] *********************")
    time.sleep(0.3)
    if run_number == 1:
        print("changed: [localhost] => {\"changed\": true, \"packages\": [\"curl\", \"jq\"]}")
    else:
        print("ok: [localhost] => {\"changed\": false, \"packages\": [\"curl\", \"jq\"]}")

    print("\nTASK [5. Ensure application container is running (Idempotent Container)] *")
    time.sleep(0.4)
    if run_number == 1:
        print("changed: [localhost] => {\"changed\": true, \"container\": {\"Name\": \"ngo-dashboard-live\", \"State\": {\"Status\": \"running\"}}}")
    else:
        print("ok: [localhost] => {\"changed\": false, \"container\": {\"Name\": \"ngo-dashboard-live\", \"State\": {\"Status\": \"running\"}}}")

    print("\nTASK [6. Wait for service health check (HTTP GET /health)] **************")
    time.sleep(0.4)
    print("ok: [localhost] => {\"status\": 200, \"content\": \"{\\\"status\\\":\\\"UP\\\",\\\"service\\\":\\\"ngo-project-dashboard\\\"}\"}")

    print("\nTASK [7. Record deployment state and version release] *******************")
    time.sleep(0.2)
    if run_number == 1:
        print("changed: [localhost] => {\"changed\": true, \"dest\": \"/opt/ngo-dashboard/releases/current_release.env\"}")
    else:
        print("ok: [localhost] => {\"changed\": false, \"dest\": \"/opt/ngo-dashboard/releases/current_release.env\"}")

    print("\nPLAY RECAP *************************************************************")
    if run_number == 1:
        print("localhost                  : ok=8    changed=6    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0")
        print("\n>>> Result Run #1: Initial configuration applied (changed=6).")
    else:
        print("localhost                  : ok=8    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0")
        print("\n>>> Result Run #2: IDEMPOTENCY CONFIRMED! Zero changes made (changed=0). System already in desired state.")

def simulate_rollback():
    print("\n")
    print_banner("PLAY [Rollback NGO Project Dashboard to Previous Version]")
    print("\nTASK [1. Announce Rollback Procedure] ***********************************")
    time.sleep(0.3)
    print("ok: [localhost] => {\"msg\": \"Initiating emergency rollback to stable image: ngo-project-dashboard:1.0.0-stable\"}")

    print("\nTASK [2. Stop and remove degraded container] ****************************")
    time.sleep(0.3)
    print("changed: [localhost] => {\"changed\": true, \"name\": \"ngo-dashboard-live\", \"state\": \"absent\"}")

    print("\nTASK [3. Launch container using previously validated stable image] ********")
    time.sleep(0.4)
    print("changed: [localhost] => {\"changed\": true, \"image\": \"ngo-project-dashboard:1.0.0-stable\", \"status\": \"running\"}")

    print("\nTASK [4. Verify post-rollback health status] ****************************")
    time.sleep(0.4)
    print("ok: [localhost] => {\"status\": 200, \"content\": \"{\\\"status\\\":\\\"UP\\\",\\\"version\\\":\\\"1.0.0-stable\\\"}\"}")

    print("\nTASK [5. Confirm rollback resolution] ***********************************")
    print("ok: [localhost] => {\"msg\": \"Rollback Complete! Application restored to healthy state running ngo-project-dashboard:1.0.0-stable\"}")

    print("\nPLAY RECAP *************************************************************")
    print("localhost                  : ok=5    changed=2    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0")
    print(">>> Result Rollback: Service restored to healthy stable release in 1.4 seconds.")

if __name__ == '__main__':
    print("Executing Ansible Idempotency and Rollback Test Suite...")
    simulate_playbook_run(1)
    time.sleep(1)
    simulate_playbook_run(2)
    time.sleep(1)
    simulate_rollback()
