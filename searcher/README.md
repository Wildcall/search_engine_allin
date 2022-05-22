## Search Engine

### Notification service

**Other services:**

- [**`crawler`**](https://github.com/Wildcall/search_engine/tree/master/crawler) 
- [**`indexer`**](https://github.com/Wildcall/search_engine/tree/master/indexer)
- [**`searcher`**](https://github.com/Wildcall/search_engine/tree/master/searcher) <
- [**`task`**](https://github.com/Wildcall/search_engine/tree/master/task_manager)
- [**`notification`**](https://github.com/Wildcall/search_engine/tree/master/notification)
- [**`registration`**](https://github.com/Wildcall/search_engine/tree/master/registration)

**Build:**

```
cd path_to_project
docker-compose up
mvn clean package repack
```

**Running:**
```
java -jar -DSEARCHER_SECRET=SEARCHER_SECRET -DTASK_MANAGER_SECRET=TASK_MANAGER_SECRET -DINDEXER_SECRET=INDEXER_SECRET -DCRAWLER_SECRET=CRAWLER_SECRET -DDATABASE_URL=postgresql://localhost:5433/se_searcher_data -DDATABASE_USER=searcher_user -DDATABASE_PASS=searcher_password
```

**Environment Variable:**

- `SEARCHER_SECRET` SEARCHER_SECRET
- `TASK_MANAGER_SECRET` TASK_MANAGER_SECRET
- `INDEXER_SECRET` INDEXER_SECRET
- `CRAWLER_SECRET` CRAWLER_SECRET
- `DATABASE_URL` postgresql://localhost:5433/se_searcher_data
- `DATABASE_USER` searcher_user
- `DATABASE_PASS` searcher_password

**Api:**

- /api/v1/
- /api/v1/searcher/start
- /api/v1/searcher/stop