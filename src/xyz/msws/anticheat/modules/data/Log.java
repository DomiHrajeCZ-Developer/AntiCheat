package xyz.msws.anticheat.modules.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

public class Log {
	private UUID uuid;
	private String token;

	private TreeMap<Long, String> log = new TreeMap<Long, String>();

	public Log(UUID uuid) {
		this.uuid = uuid;
	}

	public String getToken() {
		return token;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void insertLine(long index, String line) {
		log.put(index, line);
	}

	public void addLine(String line) {
		log.put(System.currentTimeMillis(), line);
	}

	@SuppressWarnings("unchecked")
	public List<String> getLinesFrom(long time) {
		if (time <= 0)
			return new ArrayList<>(log.values());
		long minTime = System.currentTimeMillis() - time;
		Entry<Long, String>[] arr = (Entry<Long, String>[]) log.entrySet().toArray();
		List<String> result = new ArrayList<>();
		for (int i = log.size() - 1; i >= 0; i--) {
			Entry<Long, String> v = arr[i];
			if (v.getKey() < minTime)
				break;
			result.add(v.getValue());
		}
		return result;
	}
}
