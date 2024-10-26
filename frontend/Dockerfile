FROM node:14-alpine


WORKDIR /frontend


COPY package*.json ./

RUN npm install

RUN npm ci


COPY . .


RUN npm run build 


EXPOSE 4200

CMD ["npm", "start"]
