package com.quincy.auth.o;

import java.io.Serializable;
import java.util.List;

import com.quincy.auth.entity.Menu;

import lombok.Data;

@Data
public class DSession implements Serializable {
	private static final long serialVersionUID = 997874172809782407L;
	private User user;
	private List<String> roles;
	private List<String> permissions;
	private List<Menu> menus;
}
