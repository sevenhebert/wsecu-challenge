FROM node:16.2.0-alpine

WORKDIR /app

COPY package*.json /app/package.json

RUN npm install

COPY . /app

RUN npm run build

ENV PORT=3000
ENV NODE_ENV=production

EXPOSE 3000

CMD ["npm", "start"]
