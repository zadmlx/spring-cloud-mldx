local ip = KEYS[1]
local limitCount = ARGV[1]
local ttl = ARGV[2]

local actualCount = redis.call('get',ip)
if tonumber(actualCount) > limitCount then
    return actualCount;
end

count = redis.call('incr',ip)

if tonumber(count) == 1 then
    redis.call('expire',ip,ttl)
end
