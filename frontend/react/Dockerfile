FROM node:21-alpine
ARG spring_sandbox_api_base_url

WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "VITE_API_BASE_URL=${spring_sandbox_api_base_url}" > .env
EXPOSE 5173
CMD ["npm", "run", "dev"]