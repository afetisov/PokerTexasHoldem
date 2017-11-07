package client.admin.gui;

public interface BaseEditorPanel<T> {

		void setData(T data);
		
		T getData();
}
