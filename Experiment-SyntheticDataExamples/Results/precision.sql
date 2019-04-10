select iteration, count(*) 
from tk_p_d1
where (position = 1) and (id_so =1)
group by iteration
order by iteration;