# Bank-Application

## 🛠️ Setup and Installation Guide

This guide will walk you through installing **Java 23**, **Maven**, setting up environment variables, and running the **Bank-Application** project.

---

## **🚀 Prerequisites**
Before proceeding, ensure your system has:
- Windows 10/11 (or Linux/Mac)
- Git Bash (Optional but recommended)
- VS Code or IntelliJ IDEA (Recommended for development)

---

## **🖥️ Step 1: Install Java 23**
### **🔹 Windows Installation**
1. **Download Java 23** from the official Oracle website:
   👉 [Java 23 Download](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)
2. Run the **installer** and follow the on-screen instructions.
3. **Verify installation**:
   ```sh
   java -version
   ```
   Expected output:
   ```
   openjdk version "23.0.2"
   ```

### **🔹 Linux / Mac Installation**
1. **Use SDKMAN (Recommended)**
   ```sh
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   sdk install java 23.0.2-open
   ```
2. Verify installation:
   ```sh
   java -version
   ```

---

## **🛠️ Step 2: Set Up Environment Variables for Java**
### **🔹 Windows**
1. Open **Start Menu → Search "Environment Variables"**.
2. Click **Edit the system environment variables**.
3. Under **System Variables**, click **New**:
   - **Variable Name:** `JAVA_HOME`
   - **Variable Value:** `C:\Program Files\Java\jdk-23`
4. **Edit `Path` Variable:**
   - Add: `C:\Program Files\Java\jdk-23\bin`
5. Click **OK** and restart the system.

### **🔹 Linux / Mac**
Add this to your **~/.bashrc** or **~/.zshrc**:
```sh
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH=$JAVA_HOME/bin:$PATH
```
Then, reload:
```sh
source ~/.bashrc  # or source ~/.zshrc
```

---

## **📦 Step 3: Install Maven**
### **🔹 Windows Installation**
1. Download **Maven** from:  
   👉 [Apache Maven Download](https://maven.apache.org/download.cgi)
2. Extract the **ZIP file** to `C:\Program Files\Apache\Maven`
3. **Set Up Environment Variables**:
   - Add `MAVEN_HOME`: `C:\Program Files\Apache\Maven`
   - Add `C:\Program Files\Apache\Maven\bin` to `Path`
4. **Verify installation**:
   ```sh
   mvn -version
   ```
   Expected output:
   ```
   Apache Maven 3.x.x
   ```

### **🔹 Linux / Mac**
```sh
sudo apt update
sudo apt install maven
mvn -version
```

---

## **📂 Step 4: Clone and Setup the Project**
1. **Clone the Repository**
   ```sh
   git clone https://github.com/your-repo/Bank-Application.git
   cd Bank-Application
   ```

2. **Build the Project**
   ```sh
   mvn clean package
   ```

---

## **🚀 Step 5: Run the Application**
### **Option 1: Run with Maven**
```sh
mvn exec:java
```

### **Option 2: Run the JAR File**
1. Build a JAR with dependencies:
   ```sh
   mvn clean package
   ```
2. Run the JAR file:
   ```sh
   java -jar target/Bank-Application-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

---

## **🐞 Troubleshooting**
### **1. `mvn: command not found`**
- Ensure `MAVEN_HOME` and `Path` are correctly set.
- Restart the terminal or run:
  ```sh
  source ~/.bashrc  # or ~/.zshrc
  ```

### **2. `no main manifest attribute, in jar`**
- Ensure your `pom.xml` has the **correct `Main-Class`**:
  ```xml
  <manifest>
      <mainClass>com.example.BankApplication</mainClass>
  </manifest>
  ```

### **3. `java.lang.ClassNotFoundException: com.fasterxml.jackson.databind.ObjectMapper`**
- Use the **correct JAR** with dependencies:
  ```sh
  java -jar target/Bank-Application-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```

---

## **📜 License**
This project is licensed under the MIT License.

---

## **💡 Credits**
Developed by **Gopi Krishna Maganti** 🚀
```

---

### **✅ What This README.md Covers**
✔ **Java 23 installation**  
✔ **Maven installation & setup**  
✔ **Environment variables setup (Windows, Linux, Mac)**  
✔ **Project setup & running steps**  
✔ **Troubleshooting common issues**  

Now you can **copy-paste this `README.md` into your project** and use it for reference. 🚀 Let me know if you need further refinements! 🎉
