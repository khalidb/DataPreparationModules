select iteration,  avg(@(a.recall - agt.recall)) as recall_error 
from a_d5_i a,  a_gt_d5_i agt
where (a.id_so = agt.id_so) and (agt.run = 1) and (a.recall != 'NaN') and (a.recall != 'Infinity')
group by a.iteration
order by a.iteration;