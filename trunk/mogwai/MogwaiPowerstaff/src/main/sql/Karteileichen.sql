select count(*) from freelancer_udf WHERE name = 'ansp' and stringValue is not null
select count(*) from freelancer_udf WHERE name = 'anspID' and intvalue is not null

select * from partner_udf where intvalue = -42062652
select * from customer_udf where intvalue = -42062652

select a.freelancer_id, CONCAT('\nVermittelt von ',a.stringValue) from freelancer_udf a, customer_udf b where a.name='ansp' and a.stringValue is not null

select CONCAT('Vermittelt von ',b.stringValue, '\n', a.comments) ,a.* from 
    freelancer a
    inner join freelancer_udf b on (a.id = b.freelancer_id)
where 
    b.name = 'ansp' and
    b.stringValue is not null 

update freelancer a
    inner join freelancer_udf b on (a.id = b.freelancer_id)
set
    a.comments =  concat('Vermittelt von ',b.stringValue, '\n', ifnull(a.comments,''))
where 
    b.name = 'ansp' and
    b.stringValue is not null

select a.freelancer_id, CONCAT('\nVermittelt von ',c.name1,',', c.name2, '\n' ,c.company) from freelancer_udf a, customer_udf b , customer c where a.name='anspID' and a.intvalue is not null and a.intvalue <> 0 and b.name = 'ID' and a.intvalue = b.intvalue and b.customer_id = c.id

update freelancer a
    inner join freelancer_udf b on (a.id = b.freelancer_id)
    inner join customer_udf c on (c.intvalue = b.intvalue)
    inner join customer d on (c.customer_id = d.id)
set
    a.comments = concat('Vermittelt von ',ifnull(d.name1,''),', ', ifnull(d.name2,''), ', ' ,ifnull(d.company,''),'\n',ifnull(a.comments,''))
where 
    b.name = 'anspID' and
    b.intvalue is not null and
    b.intvalue <> 0 and 
    c.name = 'ID'


select * from freelancer where code = '05152' // 17399

select concat('Vermittelt von ',b.stringValue, '\n', ifnull(a.comments, '')), b.stringValue from freelancer a
    inner join freelancer_udf b on (a.id = b.freelancer_id)
where 
    b.name = 'ansp' and
    b.stringValue is not null and
    a.id = 17399


update freelancer a
    inner join freelancer_udf b on (a.id = b.freelancer_id)
set
    a.comments =  concat('Vermittelt von ',b.stringValue, '\n', a.comments)
where 
    b.name = 'ansp' and
    b.stringValue is not null
