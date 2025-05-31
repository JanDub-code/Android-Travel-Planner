# Android-Travel-Planner

```mermaid
flowchart TB
  %%==============================
  %% Host Hardware & Virtualization
  %%==============================
  subgraph "🖥️ Host Hardware & Virtualization"
    direction TB
    Host["Ubuntu 20.04 Host\n8 fyz. jader / 16 vláken\n32 GB RAM\nKVM/QEMU Hypervisor"]
  end

  %%==============================
  %% Virtual Machines
  %%==============================
  subgraph "💠 Virtual Machines"
    direction TB
    VM1["Master VM 1\n1 vCPU, 3 GB RAM"]
    VM2["Master VM 2\n1 vCPU, 3 GB RAM"]
    VM3["Worker VM 1\n2 vCPU, 4 GB RAM"]
    VM4["Worker VM 2\n2 vCPU, 4 GB RAM"]
    VM5["Worker VM 3\n2 vCPU, 4 GB RAM"]
  end

  Host --> VM1
  Host --> VM2
  Host --> VM3
  Host --> VM4
  Host --> VM5

  %%==============================
  %% Kubernetes Cluster
  %%==============================
  subgraph "☸️ Kubernetes Cluster"
    direction TB

    %% Masters and Workers
    subgraph "🔑 Control Plane (Masters)"
      direction LR
      M1["Master Node 1\nkube-apiserver\netcd\nscheduler\ncontroller-manager"]
      M2["Master Node 2\nkube-apiserver\netcd\nscheduler\ncontroller-manager"]
    end

    subgraph "🔧 Worker Nodes"
      direction LR
      W1["Worker Node 1"]
      W2["Worker Node 2"]
      W3["Worker Node 3"]
    end

    %% Connect VMs to Kubernetes nodes
    VM1 --> M1
    VM2 --> M2
    VM3 --> W1
    VM4 --> W2
    VM5 --> W3

    %%==============================
    %% Separate services targeted by workers
    %%==============================
    subgraph "🗄️ Databases"
      direction TB
      PG["PostgreSQL Cluster"]
    end

    subgraph "🌐 Ingress & SSL"
      direction TB
      NGINX["NGINX Ingress Controller"]
      CERTM["cert-manager"]
    end

    subgraph "📊 Monitoring"
      direction TB
      PROM["Prometheus"]
      GRAF["Grafana"]
    end

    %% Arrows from workers to their services
    W1 --> PG
    W2 --> NGINX
    W2 --> CERTM
    W3 --> PROM
    PROM --> GRAF

    %% CoreDNS & CNI networking
    subgraph "🔌 CoreDNS & CNI"
      direction TB
      DNS["CoreDNS"]
      CNI["Calico CNI"]
    end

    M1 & M2 --> DNS
    DNS --> CNI
    CNI --> W1
    CNI --> W2
    CNI --> W3
    CNI --> NGINX
  end

  %%==============================
  %% External access (Internet)
  %%==============================
  NGINX -->|"Externí HTTP/HTTPS"| Internet["🌐 Internet"]
