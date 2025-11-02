# Postly - Social Blogging Platform

A full-stack social blogging platform where students can share their learning experiences, discoveries, and progress throughout their journey. Users can interact with each other's content, follow one another, and engage in meaningful discussions.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Additional Features](#additional-features)
- [Security](#security)
- [Contributing](#contributing)

## 🎯 Overview

01Blog is a comprehensive social blogging platform designed to help students document and share their learning journey. Built with a modern tech stack, it provides a secure, scalable, and user-friendly environment for content creation and social interaction.

The platform implements role-based access control with two user types:

- **Users**: Can create posts, interact with content, follow other users, and report inappropriate behavior
- **Admins**: Have full moderation capabilities including user management, content moderation, and report handling

## ✨ Features

### Core Features

#### 🔐 Authentication & Authorization

- Secure user registration and login
- JWT-based authentication
- Role-based access control (USER vs ADMIN)
- Password encryption with BCrypt
- Auto-logout on token expiration

#### 👤 User Management

- Public user profiles
- Profile customization with avatar upload
- User search functionality
- Subscribe/unsubscribe to other users
- View followers and following lists

#### 📝 Post Management

- Create, edit, and delete posts
- Rich text editor with Markdown support
- Media upload (images and videos)
- Post preview before publishing
- Timestamp and engagement metrics display
- Pagination and infinite scroll

#### 💬 Social Interactions

- Like/unlike posts
- Create, edit, and delete comments on posts
- View post engagement (likes and comments count)
- Personalized feed from subscribed users

#### 🔔 Notifications

- Real-time notifications using Server-Sent Events (SSE)
- Notification icon with unread count
- Mark notifications as read/unread
- Notifications for new posts from subscribed users

#### 🚨 Reporting System

- Report users and posts
- Detailed report submission with reasons
- Confirmation before submitting reports
- Admin-only report visibility

#### 🛡️ Admin Dashboard

- Comprehensive user management
- Post moderation and removal
- Report handling and resolution
- User ban/unban functionality
- Post hide/unhide functionality
- Analytics and statistics:
  - Total users count
  - Total posts count
  - Total reports count
  - Most reported users
  - Most reported posts
- Advanced filtering and search capabilities

### Additional Features

- **Dark Mode**: Theme toggle for comfortable viewing
- **Infinite Scroll**: Seamless content loading on feeds
- **Markdown Support**: Rich text formatting in posts
- **Responsive Design**: Mobile-friendly interface using Angular Material
- **Real-time Updates**: SSE for instant notifications
- **Advanced Search**: Filter and search users
- **Profile Analytics**: View post count, followers, and following statistics

## 🛠️ Technologies Used

### Backend

- **Java 17**: Programming language
- **Spring Boot 3.5.5**: Application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database access layer
- **Hibernate**: ORM framework
- **PostgreSQL 15**: Relational database
- **Flyway**: Database migrations
- **JWT (JSON Web Tokens)**: Stateless authentication
- **Maven**: Dependency management and build tool
- **Docker**: Containerization

### Frontend

- **Angular 20**: Frontend framework
- **TypeScript**: Programming language
- **Angular Material**: UI component library
- **RxJS**: Reactive programming
- **Angular Router**: Client-side routing
- **FormsModule & ReactiveFormsModule**: Form handling
- **Marked**: Markdown parsing
- **EasyMDE**: Markdown editor

### Development Tools

- **Docker Compose**: Multi-container orchestration
- **Git**: Version control
- **npm**: Package manager

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Node.js 18.x** or higher and npm
- **Docker** and **Docker Compose**
- **Maven 3.6+** (or use Maven wrapper included in project)
- **Git**

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd 01Blog
```

### 2. Backend Setup

#### Start the PostgreSQL Database

Navigate to the backend directory and start the database using Docker Compose:

```bash
cd backend
docker-compose up -d
```

This will create a PostgreSQL container with the following credentials:

- Database: `postly_db`
- Username: `user`
- Password: `password`
- Port: `5432`

#### Configure Application Properties

The default configuration is located in `backend/src/main/resources/application.properties`. For production, update the following:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/postly_db
spring.datasource.username=user
spring.datasource.password=password

# JWT Configuration
app.jwt.secretKey=<your-secret-key>
app.jwt.expirationTime=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=200MB
app.upload.dir=uploads
```

**Important**: Change the JWT secret key to a secure random string for production!

#### Build and Run the Backend

```bash
# Using Maven wrapper (recommended)
./mvnw clean install
./mvnw spring-boot:run

# Or using Maven directly
mvn clean install
mvn spring-boot:run
```

The backend server will start on `http://localhost:8080`

### 3. Frontend Setup

Navigate to the frontend directory:

```bash
cd ../frontend
```

#### Install Dependencies

```bash
npm install
```

#### Configure API Proxy (Development)

The frontend is configured to proxy API requests to the backend. The configuration is in `frontend/proxy.conf.json`:

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

#### Run the Frontend

```bash
npm start
```

The application will automatically open in your browser at `http://localhost:4200`

## 🎮 Running the Application

### Development Mode

1. **Start the Database**:

   ```bash
   cd backend
   docker-compose up -d
   ```

2. **Start the Backend**:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. **Start the Frontend**:

   ```bash
   cd frontend
   npm start
   ```

4. **Access the Application**:
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080

### Production Build

#### Backend

```bash
cd backend
./mvnw clean package
java -jar target/postly-0.0.1.jar
```

#### Frontend

```bash
cd frontend
npm run build
```

The production build will be created in the `frontend/dist` directory.

### Default Admin Account

The application comes with a seeded admin account:

- **Username**: `admin`
- **Email**: `admin@01blog.com`
- **Password**: `admin123`
- **Role**: ADMIN

**Important**: Change the admin password immediately after first login in production!

## 📁 Project Structure

### Backend Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/postly/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # Security configuration
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── db/migration/    # Flyway migrations
│   │       └── application.properties
│   └── test/                    # Test files
├── uploads/                     # Media uploads directory
├── docker-compose.yml           # Docker configuration
└── pom.xml                      # Maven dependencies
```

### Frontend Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/          # Reusable components
│   │   ├── directives/          # Custom directives
│   │   ├── guards/              # Route guards
│   │   ├── interceptors/        # HTTP interceptors
│   │   ├── models/              # TypeScript interfaces
│   │   ├── pages/               # Page components
│   │   ├── services/            # API services
│   │   └── app.routes.ts        # Application routes
│   ├── styles.scss              # Global styles
│   └── main.ts                  # Application entry point
├── angular.json                 # Angular configuration
├── package.json                 # npm dependencies
└── proxy.conf.json              # API proxy configuration
```

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint             | Description                 | Auth Required |
| ------ | -------------------- | --------------------------- | ------------- |
| POST   | `/api/auth/register` | Register a new user         | No            |
| POST   | `/api/auth/login`    | Login and receive JWT token | No            |

### User Endpoints

| Method | Endpoint                            | Description              | Auth Required |
| ------ | ----------------------------------- | ------------------------ | ------------- |
| GET    | `/api/users/profile`                | Get current user profile | Yes           |
| GET    | `/api/users/{username}`             | Get user by username     | Yes           |
| PUT    | `/api/users/profile`                | Update user profile      | Yes           |
| POST   | `/api/users/avatar`                 | Upload user avatar       | Yes           |
| DELETE | `/api/users/avatar`                 | Remove user avatar       | Yes           |
| GET    | `/api/users/search`                 | Search users             | Yes           |
| POST   | `/api/users/{userId}/subscribe`     | Subscribe to user        | Yes           |
| DELETE | `/api/users/{userId}/unsubscribe`   | Unsubscribe from user    | Yes           |
| GET    | `/api/users/{userId}/subscribers`   | Get user's subscribers   | Yes           |
| GET    | `/api/users/{userId}/subscriptions` | Get user's subscriptions | Yes           |

### Post Endpoints

| Method | Endpoint                   | Description           | Auth Required |
| ------ | -------------------------- | --------------------- | ------------- |
| POST   | `/api/posts`               | Create a new post     | Yes           |
| GET    | `/api/posts`               | Get paginated posts   | Yes           |
| GET    | `/api/posts/{id}`          | Get post by ID        | Yes           |
| PUT    | `/api/posts/{id}`          | Update post           | Yes (Owner)   |
| DELETE | `/api/posts/{id}`          | Delete post           | Yes (Owner)   |
| GET    | `/api/posts/user/{userId}` | Get user's posts      | Yes           |
| GET    | `/api/posts/feed`          | Get personalized feed | Yes           |
| POST   | `/api/posts/{id}/like`     | Like a post           | Yes           |
| DELETE | `/api/posts/{id}/like`     | Unlike a post         | Yes           |

### Comment Endpoints

| Method | Endpoint                       | Description       | Auth Required |
| ------ | ------------------------------ | ----------------- | ------------- |
| POST   | `/api/posts/{postId}/comments` | Create comment    | Yes           |
| GET    | `/api/posts/{postId}/comments` | Get post comments | Yes           |
| PUT    | `/api/comments/{id}`           | Update comment    | Yes (Owner)   |
| DELETE | `/api/comments/{id}`           | Delete comment    | Yes (Owner)   |

### Notification Endpoints

| Method | Endpoint                       | Description              | Auth Required |
| ------ | ------------------------------ | ------------------------ | ------------- |
| GET    | `/api/notifications/stream`    | SSE notifications stream | Yes           |
| GET    | `/api/notifications`           | Get user notifications   | Yes           |
| PUT    | `/api/notifications/{id}/read` | Mark as read             | Yes           |
| PUT    | `/api/notifications/read-all`  | Mark all as read         | Yes           |

### Report Endpoints

| Method | Endpoint                     | Description   | Auth Required |
| ------ | ---------------------------- | ------------- | ------------- |
| POST   | `/api/reports/user/{userId}` | Report a user | Yes           |
| POST   | `/api/reports/post/{postId}` | Report a post | Yes           |

### Admin Endpoints

| Method | Endpoint                          | Description                  | Auth Required |
| ------ | --------------------------------- | ---------------------------- | ------------- |
| GET    | `/api/admin/stats`                | Get dashboard statistics     | Yes (Admin)   |
| GET    | `/api/admin/users`                | Get all users with filters   | Yes (Admin)   |
| PUT    | `/api/admin/users/{id}/ban`       | Ban user                     | Yes (Admin)   |
| PUT    | `/api/admin/users/{id}/unban`     | Unban user                   | Yes (Admin)   |
| DELETE | `/api/admin/users/{id}`           | Delete user                  | Yes (Admin)   |
| GET    | `/api/admin/posts`                | Get all posts with filters   | Yes (Admin)   |
| PUT    | `/api/admin/posts/{id}/hide`      | Hide post                    | Yes (Admin)   |
| PUT    | `/api/admin/posts/{id}/unhide`    | Unhide post                  | Yes (Admin)   |
| DELETE | `/api/admin/posts/{id}`           | Delete post                  | Yes (Admin)   |
| GET    | `/api/admin/reports`              | Get all reports with filters | Yes (Admin)   |
| PUT    | `/api/admin/reports/{id}/resolve` | Resolve report               | Yes (Admin)   |
| DELETE | `/api/admin/reports/{id}`         | Delete report                | Yes (Admin)   |

## 🌟 Additional Features

### Real-time Notifications with SSE

The application uses Server-Sent Events (SSE) for real-time notifications. When a user you're subscribed to creates a new post, you'll receive an instant notification.

### Markdown Support

Posts support full Markdown formatting, allowing users to create rich, formatted content with:

- Headers
- Lists (ordered and unordered)
- Code blocks with syntax highlighting
- Links and images
- Bold, italic, and strikethrough text
- Blockquotes

### Infinite Scroll

The feed and profile pages implement infinite scroll, automatically loading more content as you reach the bottom of the page, providing a seamless browsing experience.

### Dark Mode

Toggle between light and dark themes for comfortable viewing in any lighting condition. The theme preference is stored in browser localStorage.

### Advanced Search & Filters

- Search users by username, full name, or email
- Filter posts by date, engagement, or user
- Admin dashboard includes advanced filtering for users, posts, and reports

## 🔒 Security

### Authentication

- JWT-based stateless authentication
- Secure password hashing using BCrypt
- Token expiration and validation
- Auto-logout on token expiration

### Authorization

- Role-based access control (RBAC)
- Route protection with Spring Security
- Frontend route guards
- Method-level security annotations

### Data Protection

- Input validation on all endpoints
- SQL injection prevention via JPA
- XSS protection
- CSRF protection
- CORS configuration
- File upload validation and size limits

### Password Requirements

- Minimum length: 8 characters
- Must contain at least one uppercase letter
- Must contain at least one lowercase letter
- Must contain at least one number

## 🗃️ Database Schema

The application uses PostgreSQL with Flyway migrations. Key entities include:

- **Users**: User accounts with authentication details
- **Posts**: Blog posts with content and media
- **Comments**: User comments on posts
- **Likes**: Post likes tracking
- **Subscriptions**: User following relationships
- **Notifications**: User notifications
- **Reports**: Content moderation reports

All tables include:

- Auto-generated IDs
- Timestamps (created_at, updated_at)
- Proper indexes for performance
- Foreign key constraints

## 📝 License

This project is created for educational purposes as part of a full-stack development learning journey.

## 👥 Authors

- [Yassine Elmach](https://github.com/yelmach)

---

**Happy Blogging! 📝✨**
