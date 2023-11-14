module cs2043_group_project {
	exports cs2043group10;
	exports cs2043group10.data;
	
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive javafx.base;
	
	opens cs2043group10;
}