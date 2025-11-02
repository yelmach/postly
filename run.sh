#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}   Postly Application Startup Script${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command_exists docker; then
    echo -e "${RED}Error: Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

if ! command_exists docker compose; then
    echo -e "${RED}Error: Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

if ! command_exists java; then
    echo -e "${RED}Error: Java is not installed. Please install JDK 17 or higher.${NC}"
    exit 1
fi

if ! command_exists node; then
    echo -e "${RED}Error: Node.js is not installed. Please install Node.js 18.x or higher.${NC}"
    exit 1
fi

if ! command_exists npm; then
    echo -e "${RED}Error: npm is not installed. Please install npm.${NC}"
    exit 1
fi

echo -e "${GREEN}All prerequisites are installed!${NC}\n"

# Step 1: Start PostgreSQL Database
echo -e "${BLUE}[1/4] Starting PostgreSQL database...${NC}"
cd backend
docker compose up -d
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start database. Please check Docker.${NC}"
    exit 1
fi
echo -e "${GREEN}Database started successfully!${NC}\n"

# Wait for database to be ready
echo -e "${YELLOW}Waiting for database to be ready...${NC}"
sleep 5
echo -e "${GREEN}Database is ready!${NC}\n"

# Step 2: Start Backend
echo -e "${BLUE}[2/4] Starting backend server...${NC}"
if [ -f "./mvnw" ]; then
    ./mvnw spring-boot:run &
    BACKEND_PID=$!
else
    mvn spring-boot:run &
    BACKEND_PID=$!
fi

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start backend.${NC}"
    exit 1
fi
echo -e "${GREEN}Backend is starting (PID: $BACKEND_PID)...${NC}\n"

# Wait for backend to be ready
echo -e "${YELLOW}Waiting for backend to be ready...${NC}"
sleep 20
echo -e "${GREEN}Backend should be ready!${NC}\n"

# Step 3: Install frontend dependencies (if needed)
cd ../frontend
if [ ! -d "node_modules" ]; then
    echo -e "${BLUE}[3/4] Installing frontend dependencies...${NC}"
    npm install
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to install frontend dependencies.${NC}"
        kill $BACKEND_PID
        exit 1
    fi
    echo -e "${GREEN}Frontend dependencies installed!${NC}\n"
else
    echo -e "${BLUE}[3/4] Frontend dependencies already installed.${NC}\n"
fi

# Step 4: Start Frontend
echo -e "${BLUE}[4/4] Starting frontend application...${NC}"
npm start &
FRONTEND_PID=$!

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start frontend.${NC}"
    kill $BACKEND_PID
    exit 1
fi

echo -e "${GREEN}Frontend is starting (PID: $FRONTEND_PID)...${NC}\n"

sleep 10

# Summary
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}   Postly is starting up!${NC}"
echo -e "${GREEN}========================================${NC}\n"

echo -e "${BLUE}Services:${NC}"
echo -e "  ${YELLOW}Database:${NC} PostgreSQL running in Docker"
echo -e "  ${YELLOW}Backend:${NC}  http://localhost:8080 (PID: $BACKEND_PID)"
echo -e "  ${YELLOW}Frontend:${NC} http://localhost:4200 (PID: $FRONTEND_PID)"
echo -e ""
echo -e "${BLUE}Default Admin Account:${NC}"
echo -e "  ${YELLOW}Username:${NC} admin"
echo -e "  ${YELLOW}Password:${NC} admin123"
echo -e ""
echo -e "${YELLOW}Note: The application will open automatically in your browser.${NC}"
echo -e "${YELLOW}Press Ctrl+C to stop all services.${NC}\n"

# Save PIDs to file for cleanup
echo "$BACKEND_PID" > /tmp/postly_backend.pid
echo "$FRONTEND_PID" > /tmp/postly_frontend.pid

# Wait for processes
wait
