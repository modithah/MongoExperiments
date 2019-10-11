 SELECT 
stname as collection,iter as no_queries,
              avg((col->'wiredTiger'->'cache'->>'pages read into cache')::numeric) as data_pages_read,
			  avg((col->'wiredTiger'->'cache'->>'unmodified pages evicted')::numeric) as data_pages_evicted,
		avg((col->'wiredTiger'->'cache'->>'bytes currently in the cache')::numeric) as data_bytes,
	avg((col->'indexDetails'->'_id_'->'cache'->>'pages read into cache')::numeric) as index_pages_read,
	avg((col->'indexDetails'->'_id_'->'cache'->>'unmodified pages evicted')::numeric) as index_pages_evicted,
	avg((col->'indexDetails'->'_id_'->'cache'->>'bytes currently in the cache')::numeric) as index_bytes
	
	
              
              FROM public."NewExp" group by stname,iter order by stname,iter;