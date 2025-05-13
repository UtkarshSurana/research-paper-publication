# 📝 Research Paper Publication Web Application

A RESTful web application built with **Spring Boot** and **MySQL**, designed to streamline the submission, review, and publication of research papers. The platform features secure authentication, role-based access control, and dynamic content management for administrators.

---

## 🚀 Features

- 🔐 **JWT-Based Authentication**
  - Secures endpoints using stateless JWT tokens.
  - Handles login and token refresh securely.

- 👥 **Role-Based Authorization**
  - **Admin**: Manages users, topics, and approves research papers.
  - **Author**: Submits and manages personal paper submissions.
  - **Registered User**: Browses, downloads, and comments on published papers.

- 📄 **Paper Submission and Publication Workflow**
  - Authors can upload research papers for review.
  - Admin reviews and approves/rejects papers before publication.

- 💬 **Commenting System**
  - Registered users can leave comments on published research papers.

- 📚 **Dynamic Topic Management**
  - Admins can create, edit, and delete research topics.

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot
- **Database**: MySQL
- **Security**: JWT (JSON Web Token) for authentication and authorization

---
