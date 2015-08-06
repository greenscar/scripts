SELECT 
app_instances.id, 
process_instances.id, process_instances.http_port, process_instances.jmx_port, process_instances.active, 
servers.id, servers.name, 
customers.id, customers.name, 
processes.id, processes.name, 
dbs.id, dbs.name, 
groups.id, groups.name 
FROM customers, apps, servers, db_2_process_instances, groups, processes, app_instances, dbs, process_instances 
WHERE (((((process_instances.servers_id=servers.id AND processes.id=process_instances.processes_id) 
AND process_instances.customers_id=customers.id) AND groups.id=processes.groups_id) 
AND (process_instances.id=db_2_process_instances.process_instances_id AND dbs.id=db_2_process_instances.dbs_id)) 
AND (app_instances.apps_id=apps.id AND app_instances.process_instances_id=process_instances.id)) 
GROUP BY apps.id ORDER BY customers.name;


SELECT 
process_instances.id, process_instances.http_port, process_instances.jmx_port, process_instances.active, 
customers.id, customers.name, 
processes.id, processes.name, 
servers.id, servers.name, 
dbs.id, dbs.name,
groups.id, groups.name 
FROM customers, apps, servers, groups, processes, process_instances, dbs, db_2_process_instances
WHERE(
      (
       (process_instances.servers_id=servers.id AND processes.id=process_instances.processes_id) 
       AND
	  process_instances.customers_id=customers.id
      ) 
      AND 
      groups.id=processes.groups_id
     ) 
GROUP BY process_instances.id ORDER BY customers.name;



select 
processes.id AS SERVICE_ID, processes.name AS SERVICE_NAME,
customers.id AS C_ID, customers.name AS C_NAME, 
servers.id AS SERVER_ID, servers.name as SERVER_NAME,
groups.name, 
process_instances.id AS SI_ID, process_instances.http_port, process_instances.jmx_port, process_instances.active, 
db_2_process_instances.id AS D2SI_ID,
dbs.name AS DB_NAME
from servers, groups, processes, customers, process_instances process_instances
LEFT OUTER JOIN     db_2_process_instances
ON             process_instances.id = db_2_process_instances.process_instances_id
LEFT OUTER JOIN     dbs
ON             dbs.id = db_2_process_instances.dbs_id
where processes.id = process_instances.processes_id
and servers.id = process_instances.servers_id
and customers.id = process_instances.customers_id
and groups.id = processes.groups_id
and processes.id = 1;
