//      Petrozavodsk-2011
//
//	Graph Reachability, Offline
//	O(n + k log k alpha(k))
//	Author: Kazuhiro Hosaka

import static java.lang.Math.*;
import static java.math.BigInteger.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import java.math.*;
import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		new Main().run();
	}
	Scanner in = new Scanner(System.in);
	
	void run() {
		for (; in.hasNext(); ) {
			int n = in.nextInt(), k = in.nextInt();
			Query[] qs = new Query[k];
			for (int i = 0; i < k; ++i) {
				qs[i] = new Query(in.nextInt(), in.nextInt(), in.nextInt());
			}
			Map<Pair,Integer> edges = new TreeMap<Pair,Integer>();
			for (int i = 0; i < k; ++i) {
				if (qs[i].t == 1) {
					edges.put(new Pair(qs[i].a, qs[i].b), i);
					qs[i].addTime = i;
					qs[i].remTime = k;
				} else if (qs[i].t == 2) {
					int j = edges.get(new Pair(qs[i].a, qs[i].b));
					qs[j].addTime = qs[i].addTime = j;
					qs[j].remTime = qs[i].remTime = i;
				}
			}
			solve(n, qs);
			for (int i = 0; i < k; ++i) {
				if (qs[i].t == 3) {
					System.out.println(qs[i].answer ? "YES" : "NO");
				}
			}
		}
	}
	void solve(int n, Query[] qs) {
		int k = qs.length;
		if (k == 1) {
			if (qs[0].t == 3) {
				qs[0].answer = (qs[0].a == qs[0].b);
			}
		} else {
			for (int part = 0; part < 2; ++part) {
				int x, y;
				if (part == 0) {
					x = 0;
					y = k / 2;
				} else {
					x = k / 2;
					y = k;
				}
				UnionFind uf = new UnionFind(n);
				for (int i = 0; i < k; ++i) {
					if (qs[i].t == 1 || qs[i].t == 2) {
						if (qs[i].addTime <= x && y <= qs[i].remTime) {
							uf.connect(qs[i].a, qs[i].b);
						}
					}
				}
				int nn = 0;
				int[] nums = new int[n];
				fill(nums, -1);
				for (int i = x; i < y; ++i) {
					int u = uf.root(qs[i].a), v = uf.root(qs[i].b);
					if (nums[u] == -1) {
						nums[u] = nn++;
					}
					if (nums[v] == -1) {
						nums[v] = nn++;
					}
				}
				Query[] qsSub = new Query[y - x];
				for (int i = x; i < y; ++i) {
					qsSub[i - x] = new Query(qs[i].t, nums[uf.root(qs[i].a)], nums[uf.root(qs[i].b)]);
					if (qs[i].t == 1 || qs[i].t == 2) {
						qsSub[i - x].addTime = qs[i].addTime - x;
						qsSub[i - x].remTime = qs[i].remTime - x;
					}
				}
				solve(nn, qsSub);
				for (int i = x; i < y; ++i) {
					if (qs[i].t == 3) {
						qs[i].answer = qsSub[i - x].answer;
					}
				}
			}
		}
	}
	class Query {
		int t, a, b;
		int addTime, remTime;
		boolean answer;
		Query(int t, int a, int b) {
			this.t = t;
			this.a = a;
			this.b = b;
		}
	}
	class Pair implements Comparable<Pair> {
		int a, b;
		Pair(int a, int b) {
			this.a = a;
			this.b = b;
		}
		public int compareTo(Pair p) {
			return (a < p.a) ? -1 : (a > p.a) ? +1 : (b < p.b) ? -1 : (b > p.b) ? +1 : 0;
		}
	}
	class UnionFind {
		int[] uf;
		UnionFind(int n) {
			uf = new int[n];
			fill(uf, -1);
		}
		int root(int u) {
			return (uf[u] < 0) ? u : (uf[u] = root(uf[u]));
		}
		void connect(int u, int v) {
			u = root(u);
			v = root(v);
			if (u == v) {
				return;
			}
			if (uf[u] > uf[v]) {
				int tmp = u;
				u = v;
				v = tmp;
			}
			uf[u] += uf[v];
			uf[v] = u;
		}
	}
	
	void _out(Object...os) {
		System.out.println(deepToString(os));
	}
}

