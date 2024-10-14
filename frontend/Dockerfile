# Use Node.js as the base image
FROM node:18.17.0

# Set the working directory
WORKDIR /frontend

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install -g npm@10.9.0

# Copy the rest of the application code
COPY . .

# Expose the port that Angular serves on
EXPOSE 4200

# Start the Angular application with ng serve
CMD ["npm", "start"]
