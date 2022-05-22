## Search Engine

### Notification service

**Other services:**

- [**`crawler`**](https://github.com/Wildcall/search_engine/tree/master/crawler) 
- [**`indexer`**](https://github.com/Wildcall/search_engine/tree/master/indexer)
- [**`searcher`**](https://github.com/Wildcall/search_engine/tree/master/searcher)
- [**`task`**](https://github.com/Wildcall/search_engine/tree/master/task_manager)
- [**`notification`**](https://github.com/Wildcall/search_engine/tree/master/notification)
- [**`registration`**](https://github.com/Wildcall/search_engine/tree/master/registration) <

**Build:**

```
cd path_to_project
docker-compose up
mvn clean package repack
```

**Running:**
```
java -jar -DREGISTRATION_SECRET=REGISTRATION_SECRET -DNOTIFICATION_SECRET=NOTIFICATION_SECRET -DDATABASE_URL=postgresql://localhost:5432/se_reg_data -DDATABASE_USER=reg_user -DDATABASE_PASS=reg_password
```

**Environment Variable:**

- `REGISTRATION_SECRET` REGISTRATION_SECRET
- `NOTIFICATION_SECRET` NOTIFICATION_SECRET
- `DATABASE_URL` postgresql://localhost:5432/se_reg_data
- `DATABASE_USER` reg_user
- `DATABASE_PASS` reg_password

**Api:**

- /api/v1/user/registration
- /api/v1/user/refresh
- /api/v1/user/confirm
- /api/v1/user/status
- /api/v1/user