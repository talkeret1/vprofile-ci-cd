⬅️ [Back to README](../README.md)

---

# VProfile - About the Application

<br>

The application serves as an integration validation platform.

Each feature intentionally interacts with a specific infrastructure component, allowing verification that all services are correctly deployed, connected, and operational.

<br>

## 🧪 Infrastructure Validation Scenarios

<br>

### 1. *Database Validation:*
**Login page:**
- Username: `admin_vp`
- Password: `admin_vp`

If login succeeds → MySQL connection is working.

<p align="left">
  <img src="./images/Application_Login_Page.png" alt="Login Page" width="400">
</p>

<br>

---

<br>

After login page, on the main page, you will see the following 3 buttons to test the infrastructure components:

<p align="left">
  <img src="./images/Application_Buttons.png" alt="Buttons" width="700">
</p>

1. **All Users** button → verifies Memcached.
2. **RabbitMQ** button → verifies RabbitMQ.
3. **Index Users** button → verifies Elasticsearch/OpenSearch.

<br>

---

<br>

### 2. *Memcached Validation:*
**After login:**
- Click **All Users** button:
- Click on one of the users Id's (to open a user profile)
- Go back to the previous page and click again on the same user Id

You will see:
- First load: **[Data is From DB and Data Inserted In Cache !!]**
- Second load: **[Data is From Cache]**

This confirms Memcached caching works.

Example screenshot:

<p align="left">
  <img src="./images/Application_Validation_Memcached.png" alt="Memcached Validation" width="850">
</p>

<br>

---

<br>

### 3. *RabbitMQ Validation:*
Click **RabbitMQ** button:

Example screenshot of the expected output:

<p align="left">
  <img src="./images/Application_Validation_RabbitMq.png" alt="RabbitMq Validation" width="300">
</p>

<br>

---

<br>

### 4. *OpenSearch Validation:*
Click **Index Users** button:

Example screenshot of the expected output:

<p align="left">
  <img src="./images/Application_Validation_OpenSearch.png" alt="OpenSearch Validation" width="400">
</p>

<br>

---

<br>

⬅️ [Back to README](../README.md)