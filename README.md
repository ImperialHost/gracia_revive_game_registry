![Java](https://img.shields.io/badge/Java-21-red?logo=java)
![Ant](https://img.shields.io/badge/Build-Apache%20Ant-blue?logo=apache-ant)
![Eclipse](https://img.shields.io/badge/IDE-Eclipse-2C2255?logo=eclipse)
![MariaDB](https://img.shields.io/badge/Database-MariaDB-003545?logo=mariadb)
![Platform](https://img.shields.io/badge/OS-Windows%2011-0078D6?logo=windows)

# Gracia Game Registry – Admin Tool

**Gracia Game Registry** is a standalone administration tool.  
It is responsible for registering GameServer instances into the database and generating the cryptographic identity required for authentication with the LoginServer.

---

## 📌 Overview

The **gracia_revive_game_registry** tool handles the creation and storage of **binary HexID keys (SHA-256, 32 bytes)** used by GameServers to securely authenticate against the LoginServer.

Each registered server receives a unique cryptographic identity stored in the `gameservers` database table.

---

## ⚙️ Architecture Overview

The system is split into clean layers:

### 🚀 Bootstrap
- `RegistryBootstrap`  
Initial entry point of the application.

### ⚙️ Configuration
- `RegistryConfig`
- `RegistryLoader`  
Handles loading database + runtime configuration.

### 🧠 Business Logic
- `GameServerService`  
Core logic for registering and managing servers.

### 🔐 Security
- `ServerIdentityGenerator`  
Generates SHA-256 HexID (32 bytes).

### 🗄 Infrastructure
- `DataSourceProvider` → DB connection pool (HikariCP)
- `FileIO`, `FileIOException`
- `ServerNameLoader`

### 🌐 UI (Console)
- `RegistryCli`  
Interactive command-line interface.

### 🌍 Internationalization
- `MessageService`
- `messages_en.properties`
- `messages_ro.properties`


## 🔐 Security Model

- Algorithm: **SHA-256**
- Key size: **256-bit (32 bytes)**
- Storage format: **VARBINARY(32)**
- Purpose: Secure GameServer ↔ LoginServer authentication

---

## 🛠 Build / Compile Instructions

This section explains how to build the **Gracia Game Registry** tool from source.


### 1️⃣ Prerequisites

Make sure the following are installed and properly configured:

- **Liberica Standard JDK 21 LTS** – required for compilation and runtime  
- **Apache Ant** – build automation tool  
- **Eclipse IDE** (optional, recommended for development)  
- **MariaDB 11.8 (LTS)** – database server for persistence  
- **Windows 11** – primary tested operating system 

---

## 📄 NOTICE – Legal Disclaimer

**Purpose**  
This repository and its contents are provided solely for educational, technical, and research purposes.  
They are **not intended** for the operation, promotion, or support of commercial private servers.

**Terms of Use**  
The authors and maintainers of this repository **do not endorse or encourage** the use of this code in any manner that would violate the Terms of Service of Lineage II, applicable copyright laws, or other relevant regulations.

**Redistribution and Reuse**  
Any redistribution, modification, or reuse of this code must:  
1. Retain this Notice in full.  
2. Comply with the licensing terms of this project.  

**Limitation of Liability**  
The authors provide this code "as-is" without warranty of any kind. Use at your own risk. They are **not responsible** for any legal, financial, or operational consequences arising from its misuse.

---

## 📄 License & Notice
  
See the following files for details:  
- [LICENSE.md](LICENSE.md)  
- [NOTICE_EN.md](NOTICE_EN.md) (English)  
- [NOTICE_RO.md](NOTICE_RO.md) (Română)