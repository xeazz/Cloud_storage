FROM node:latest
WORKDIR /usr/src/cloud
COPY package*.json ./
EXPOSE 8080
RUN npm install
COPY . .
CMD ["npm",  "run", "serve"]