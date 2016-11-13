# Using the CoCoME UI

Once you have successfully deployed the projects you can access the 
frontend under the following URL:

`http://${web.host}:${web}.${web.httpPort}/cloud-web-frontend`
  (eg.  http://localhost:8080/Cloud-web-frontend)

Note that `${web.host}` and `${web.httpPort}` are the values you
specified in the `settings.xml` (see `Development Setup.md`)

A login screen will appear where you can log in with one of 
the following credentials:

- User: admin, Password: admin
- User: cashier, Password: cashier
- User: enterprisemanager, Password: enterprise
- User: storemanager, Password: store
- User: stockmanager, Password: store

The credentials are hardcoded at this time but this will be 
changed in the future.

On first start it is recommended that you select Database Manager 
from the dropdown menu at the top and log in as admin. Now you 
can add enterprises, stores and products to the database by using 
the frontend.

You can also log in as a Cashier with the admin user or the 
cashier user to process sales through the web frontend. 
You need to provide the ID of an existing store to do that
which can be retrieved using the Database Manager view mentioned 
in the paragraph above.

