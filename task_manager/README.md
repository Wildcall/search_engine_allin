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
java -jar -DTASK_MANAGER_SECRET=TASK_MANAGER_SECRET -DREGISTRATION_SECRET=REGISTRATION_SECRET -DNOTIFICATION_SECRET=NOTIFICATION_SECRET -DCRAWLER_SECRET=CRAWLER_SECRET -DINDEXER_SECRET=INDEXER_SECRET -DSEARCHER_SECRET=SEARCHER_SECRET -DDATABASE_URL=postgresql://localhost:5434/se_task_data -DDATABASE_USER=task_user -DDATABASE_PASS=task_password
```

**Environment Variable:**

- `TASK_MANAGER_SECRET` TASK_MANAGER_SECRET
- `REGISTRATION_SECRET` REGISTRATION_SECRET
- `NOTIFICATION_SECRET` NOTIFICATION_SECRET
- `CRAWLER_SECRET` CRAWLER_SECRET
- `INDEXER_SECRET` INDEXER_SECRET
- `SEARCHER_SECRET` SEARCHER_SECRET
- `DATABASE_URL` postgresql://localhost:5434/se_task_data
- `DATABASE_USER` task_user
- `DATABASE_PASS` task_password

**Api:**

- /api/v1/site
- /api/v1/site/{id}
- /api/v1/setting
- /api/v1/setting/{id}
- /api/v1/setting/{id}
- /api/v1/setting/type/{type}
- /api/v1/setting/crawler
- /api/v1/setting/indexer
- /api/v1/setting/searcher
- /api/v1/resource/start/{id}
- /api/v1/resource/stop/{id}
- /api/v1/task
- /api/v1/task/{id}
- /api/v1/task/type/{type}
- /api/v1/task/site/{id}
- /api/v1/task/status/{status}