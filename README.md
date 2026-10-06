# NGO Project Progress Dashboard 🌍

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-green.svg)]()
[![Docker](https://img.shields.io/badge/Docker-Multi--stage-blue.svg)]()
[![Ansible](https://img.shields.io/badge/Ansible-Idempotent-red.svg)]()
[![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)]()

> **Comprehensive DevOps Capstone Project:** 15 weeks compressed into a production-grade, audited CI/CD pipeline, automated Selenium regression testing, multi-stage Docker containerization, and idempotent Ansible configuration management.

---

## 📋 Table of Contents
1. [Overview & Problem Statement](#overview--problem-statement)
2. [Tech Stack & Architecture](#tech-stack--architecture)
3. [Key Features](#key-features)
4. [Quick Start (Local Running)](#quick-start-local-running)
5. [Automated Testing Suite (JUnit & Selenium)](#automated-testing-suite)
6. [DevOps Pipeline (Jenkinsfile)](#devops-pipeline)
7. [Containerization (Docker & Compose)](#containerization)
8. [Configuration Management (Ansible)](#configuration-management)
9. [Documentation Roadmap](#documentation-roadmap)

---

## 1. Overview & Problem Statement
Non-profit organizations manage high-impact social initiatives across water sanitation, education, healthcare, and livelihood support. Without centralized velocity tracking, projects encounter silent budget burnouts and overdue deadlines. 

The **NGO Project Progress Dashboard** delivers:
- Executive portfolio metrics and progress tracking.
- Proactive early-warning alerts for overdue and at-risk programs.
- End-to-end automated DevOps quality gates ensuring zero regressions reach production.

---

## 2. Tech Stack & Architecture
- **Backend:** Java 21, Spring Boot 3.2.4, Spring Data JPA, Spring Boot Actuator
- **Frontend:** Thymeleaf template engine, HTML5, CSS3, Bootstrap 5
- **Database:** H2 Database Engine (embedded in-memory with `/h2-console` audit)
- **Continuous Integration:** Jenkins (Declarative multi-stage pipeline)
- **Automated Testing:** JUnit 5, MockMvc, Selenium WebDriver 4 (Headless Chrome)
- **Containerization:** Docker (Multi-stage build, minimal JRE runtime), Docker Compose
- **Configuration Management:** Ansible (Playbooks, inventory, idempotency checks, automated rollback)

---

## 3. Key Features
- **Executive Dashboard:** Dynamic KPI metric cards (Total, In-Progress, Completed, Delayed, Beneficiaries, Budget/Spent).
- **Milestone & Event Logging:** Log project milestones and dynamically recalculate progress.
- **Critical Risk Alerts:** Visual warning banner and `/alerts` page detecting delayed initiatives.
- **DevOps Health Observability:** Standardized `/health` and `/actuator/health` JSON endpoints.

---

## 4. Quick Start (Local Running)

### Prerequisites
- JDK 17 or 21
- Apache Maven 3.9+
- Google Chrome or Chromium (for Selenium tests)

### Build & Run
```bash
# Clone the repository
git clone https://github.com/your-org/ngo-project-dashboard.git
cd ngo-project-dashboard

# Set JAVA_HOME (Windows PowerShell example)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.11"

# Run locally using Spring Boot Maven plugin
mvn spring-boot:run
```
Access the application:
- **Web Dashboard:** [http://localhost:8080/](http://localhost:8080/)
- **H2 Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:ngodb`, User: `sa`, Password: `password`)
- **Health Check Endpoint:** [http://localhost:8080/health](http://localhost:8080/health)

---

## 5. Automated Testing Suite
Run all unit and MockMvc integration tests:
```bash
mvn test
```

Run headless Selenium UI regression tests:
```bash
mvn test -Dtest=*SeleniumTest
```
*Screenshots on any test failure are automatically stored under `screenshots/`.*

---

## 6. DevOps Pipeline
The declarative `jenkins/Jenkinsfile` orchestrates:
1. **Checkout:** Clones Git repository.
2. **Build & Unit Test:** Compiles code and verifies logic with JUnit 5.
3. **Package:** Creates production `.jar` / `.war` artifact.
4. **Selenium UI Test Gate:** Runs headless Chrome UI tests against ephemeral test instance.
5. **Docker Build & Tag:** Multi-stage image build tagged with `1.0.${BUILD_NUMBER}`.
6. **Deploy & Health Check:** Deploys container and curls `/health`.

---

## 7. Containerization
Build and run using Docker:
```bash
# Build Docker image
docker build -t ngo-project-dashboard:latest -f docker/Dockerfile .

# Run container with port forwarding
docker run -d -p 8080:8080 --name ngo-dashboard ngo-project-dashboard:latest

# Check health
curl http://localhost:8080/health
```

---

## 8. Configuration Management
Provision server and run container with Ansible:
```bash
cd ansible
ansible-playbook -i hosts.ini playbook.yml
```
Verify idempotency: re-running returns `changed=0`.

In case of unhealthy deployment, trigger instant rollback:
```bash
ansible-playbook -i hosts.ini rollback.yml -e "ROLLBACK_VERSION=1.0.0"
```

---

## 9. Documentation Roadmap
- [Inception & MVP Scope](file:///docs/01-inception-and-scope.md)
- [User Stories & Acceptance Criteria](file:///docs/02-user-stories.md)
- [Kanban Board & Definition of Done](file:///docs/03-kanban-and-dod.md)
- [DevOps Lifecycle Architecture](file:///docs/04-devops-lifecycle.md)
- [SRS & System Architecture](file:///docs/05-srs-and-architecture.md)
- [Git Branching Strategy](file:///docs/06-branching-strategy.md)
- [Automated Test Plan](file:///docs/07-test-plan.md)
- [Server Prerequisites](file:///docs/08-server-prerequisites.md)
- [Final Project Report](file:///docs/FINAL_PROJECT_REPORT.md)
- [Presentation Slide Deck](file:///docs/PRESENTATION_SLIDES.md)
- [Viva Voce Preparation Guide](file:///docs/VIVA_PREPARATION_GUIDE.md)
