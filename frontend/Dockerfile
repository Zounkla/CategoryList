FROM node:14-alpine


WORKDIR /frontend


COPY package*.json ./


RUN npm ci


COPY . .


RUN npm run build --prod


EXPOSE 4200

CMD ["npm", "start"]
