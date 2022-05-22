## Search Engine

### Notification service

**Other services:**

- [**`crawler`**](https://github.com/Wildcall/search_engine/tree/master/crawler) 
- [**`indexer`**](https://github.com/Wildcall/search_engine/tree/master/indexer)
- [**`searcher`**](https://github.com/Wildcall/search_engine/tree/master/searcher)
- [**`task`**](https://github.com/Wildcall/search_engine/tree/master/task_manager)
- [**`notification`**](https://github.com/Wildcall/search_engine/tree/master/notification) <
- [**`registration`**](https://github.com/Wildcall/search_engine/tree/master/registration)

**Build:**

```
mvn clean package repack
```

**Running:**
```
java -jar -DNOTIFICATION_SECRET=NOTIFICATION_SECRET -DMAIL_USERANAME=***** -DMAIL_PASSWORD=*****
```

**Environment Variable:**

- `NOTIFICATION_SECRET` NOTIFICATION_SECRET
- `MAIL_USERANAME` *****
- `MAIL_PASSWORD` *****

**Api:**

- /api/v1/notification/send