# 📸 VISUAL by EVA — Camera & Filter Showcase App

A modern Android demo app built with **Jetpack Compose**, **CameraX**, and **Clean Architecture**.  
The goal of this project is to showcase image capturing, editing, and sharing capabilities — including real-time filters and gallery support.

---

## 🚀 Features

### 📷 Camera Functionality
- Take photos using the **front**, **back**, and **additional device cameras** (e.g. macro or wide-angle, if available)
- **Switch between available cameras** dynamically
- **Save captured images** to device storage
- **Real-time filter preview** while using the camera

### 🖼 Image Handling
- Apply **visual filters** to images (e.g. sepia, cinematic, black & white, vibrant)
- Load images from:
  - Device gallery
  - 📡 **Unsplash API** (via Retrofit) — [🔴 Bonus Feature]
- Save filtered images to gallery
- Share filtered images via any compatible app (social media, chat, etc.)

---

## 🧠 Tech Stack

- **Jetpack Compose** (UI)
- **CameraX** (image capture)
- **Coil** (image loading)
- **Retrofit** + **Gson** (networking for Unsplash)
- **Hilt** (DI)
- **Flow / StateFlow / SharedFlow** (event-driven state management)
- **MediaStore + FileProvider** (saving & sharing)
- **Clean Architecture** (modular, testable, scalable)

---
