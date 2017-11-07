create trigger trg_update_table_stat after insert on HAND_TABLE 
for each row
begin

	declare handID, tableID bigint;
	declare handRake, avgPot, avgRake decimal(19,2);
	declare avgTime int;
	
	set handID = NEW.hand_id;
	set tableID = NEW.table_id;
	
	-- 1. find the hand for the table

	select rake into handRake from HANDS_HISTORY where id = handID;	
	
	-- 2. calculate avg pot, rake, hands/hr, total rake

	select avg(tbl.totalPot), avg(tbl.rake), avg(tbl.handTime) into avgPot, avgRake, avgTime 
	from (
		select hh.totalPot, hh.rake, timestampdiff(minute,hh.startDateTime, hh.finishDateTime) handTime
		from HANDS_HISTORY hh
		inner join HAND_TABLE ht on ht.hand_id = hh.id
		where ht.table_id = tableID
		order by hh.id desc
		limit 3
	) tbl;

	
	-- 3. update the table
	
	update TABLES set averagePot = round(avgPot), averageRake = avgRake, totalRake = totalRake + handRake, handsPerHour = 60 / ifnull(nullif(avgTime,0),1) where id = tableID;
	
end