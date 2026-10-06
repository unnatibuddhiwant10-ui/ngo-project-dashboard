# Server Prerequisites & Infrastructure Specification

## 1. Target Host Specifications
| Component | Minimum Specification | Recommended Production Spec |
| :--- | :--- | :--- |
| **Operating System** | Ubuntu 22.04 LTS / Debian 12 / RHEL 9 | Ubuntu 22.04 LTS x86_64 |
| **CPU** | 1 vCPU (2.0 GHz+) | 2 vCPU |
| **RAM** | 2 GB | 4 GB |
| **Storage** | 15 GB SSD | 30 GB SSD |
| **Network** | 100 Mbps NIC | 1 Gbps NIC with Static IP / FQDN |

---

## 2. Operating System & Software Prerequisites
- **Container Engine:** Docker Engine 24.0+ or Podman 4.5+
- **Container Management:** Docker Compose v2.20+
- **Configuration Management Client:** Python 3.10+, Ansible core 2.14+
- **Network Diagnostic Utilities:** `curl`, `net-tools`, `lsof`, `jq`

---

## 3. Dedicated System Accounts & Groups
For enterprise least-privilege compliance, containers and system files must not run under root:
- **Group:** `ngogroup` (GID: 1001)
- **User:** `ngoapp` (UID: 1001, shell: `/bin/bash`, home: `/opt/ngo-dashboard`)
- **Docker Group Membership:** `usermod -aG docker ngoapp`

---

## 4. Directory Structure & Permissions
```
/opt/ngo-dashboard/
├── bin/                 # Startup, stop, and rollback scripts (rwxr-xr-x ngoapp:ngogroup)
├── config/              # Externalized application.properties overrides
├── logs/                # Persistent container logs (rwxrwxr-x ngoapp:ngogroup)
└── releases/            # Release tag lock files & state tracking
```

---

## 5. Network Ports & Firewall Rules (UFW / Iptables)
| Port | Protocol | Source | Service Description |
| :--- | :--- | :--- | :--- |
| **22** | TCP | Management Subnet | Secure Shell (SSH) Administration |
| **8080** | TCP | Load Balancer / Ingress | NGO Project Dashboard HTTP Web Traffic |
| **8081** | TCP | Internal DevOps | Jenkins CI/CD Controller |
| **8082** | TCP | Internal Staging | Tomcat 10 Testing Container |

```bash
# UFW Firewall Configuration
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp
sudo ufw allow 8080/tcp
sudo ufw enable
```
