# 01 - Java Maven App with GitHub Actions CI

A tiny Spring Boot app plus a CI pipeline that builds, tests, containerises and security-scans it.

## What it demonstrates
- Maven build lifecycle (`mvn verify`) and a JUnit test
- Multi-stage, non-root Dockerfile
- GitHub Actions workflow with **Trivy** scans (filesystem and image) that fail on HIGH/CRITICAL findings

## Pipeline
```
push / PR -> Maven build + test -> Trivy fs scan -> Docker build -> Trivy image scan
```

## Run locally
```bash
# with Maven + JDK 17
mvn spring-boot:run

# or with Docker only
docker build -t demo-app:local .
docker run --rm -p 8080:8080 demo-app:local
```
Then open <http://localhost:8080/> and <http://localhost:8080/actuator/health>.

## Run the Trivy scan locally
```bash
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image demo-app:local
```

## Verification logs (`logs/`)
| Folder / file | What it shows |
|---|---|
| `maven-verify.log`, `docker-build.log`, `trivy-image-scan.log`, `container-test.log` | Current state: Spring Boot 4.1.2, build passes, Trivy reports 0 HIGH/CRITICAL, container endpoints healthy |
| `before-upgrade-boot-3.3.5/` | Original Trivy scan: 40 HIGH/CRITICAL (9 critical) in Java dependencies |
| `intermediate-boot-3.5.16/` | After the first bump: 13 remaining (7 critical), one fixed only in Spring Framework 7 |
| `attempts/` | Failed early runs (Docker not running, disk I/O error, Trivy timeout) |

Spring Boot 4 notes: starter renamed to `spring-boot-starter-webmvc`, and `@WebMvcTest` moved to
`org.springframework.boot.webmvc.test.autoconfigure` (needs `spring-boot-starter-webmvc-test`).

## Ideas to extend
- Push the image to Amazon ECR using GitHub OIDC (no stored AWS keys)
- Add a SonarQube/SonarCloud step
- Add Dependabot for Maven and Actions updates
