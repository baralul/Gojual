# 📖 About The Project

**Gojual** is an Android-based marketplace app designed to facilitate direct transactions between users for goods and services.

- 🔍 Users can *browse listings* without signing in.
- 🔐 Authentication is required for posting and contacting sellers.
- 🛠️ Built with **modern Android architecture** and **Firebase backend**.

---

## ✨ Key Features

- **User Authentication**: Firebase Email/Password login/logout.
- **Browse Listings**: Anonymous viewing supported.
- **Pagination**: Infinite scroll (10 items per load).
- **Real-time Search**: Firestore query by post title.
- **Create Postings**: Logged-in users can post ads.
- **Dynamic Categories**: Based on type (Goods/Services).
- **Post Detail View**: Full info per post.
- **Direct Contact**: WhatsApp, phone, or email (visible to authenticated users).
- **User Profile**: View and manage session.
- **Modern UI**: Material Design 3 + ConstraintLayout.

---

## 🛠️ Tech Stack & Architecture

### Built With
- **Language**: Kotlin
- **UI**: Android XML
- **Async**: Kotlin Coroutines
- **Backend**: Firebase Authentication + Firestore
- **Navigation**: Jetpack Navigation Component
- **Dependencies**: Gradle (with `libs.versions.toml`)

### Architecture: MVVM
- **View**: Displays data, handles input (Activity/Fragment)
- **ViewModel**: UI logic via LiveData
- **Repository**: Data access layer (Firestore)

---

## 🚀 Getting Started

### Clone the Repo

```bash
git clone https://github.com/YOUR_USERNAME/gojual.git
```

### Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com) and create a project.
2. Enable:
   - Authentication → Email/Password
   - Firestore Database (test mode)
3. Register Android app (package: `com.tam.gojual`)
4. Download `google-services.json` and place it in the `app/` folder.

---

### Set Up Firestore Security Rules (for development)

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

---

### Create Composite Index

In Firestore → Indexes tab → Create Composite Index:

- Collection: `posts`
- Fields:
  - `title` → Ascending
  - `createdAt` → Descending

Wait until the index is "Enabled".
