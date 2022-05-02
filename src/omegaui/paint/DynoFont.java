package omegaui.paint;
import java.awt.Font;
public class DynoFont extends Font{
	private static final short DEFAULT_FONT_SIZE = 12;
	 
	public DynoFont(String name){
		this(name, DEFAULT_FONT_SIZE);
	}

	public DynoFont(String name, int size){
		super(name, PLAIN, size);
	}

	public DynoFont(String name, int style, int size){
		super(name, style, size);
	}

	public DynoFont bold(){
		return new DynoFont(getName(), BOLD, getSize());
	}

	public DynoFont italic(){
		return new DynoFont(getName(), ITALIC, getSize());
	}

	public DynoFont italicBold(){
		return new DynoFont(getName(), ITALIC + BOLD, getSize());
	}

	public static DynoFont font(String name){
		return new DynoFont(name);
	}

	public static DynoFont font(String name, int size){
		return new DynoFont(name, size);
	}

	public static DynoFont font(String name, int style, int size){
		return new DynoFont(name, style, size);
	}

	public static DynoFont boldFont(String name, int size){
		return new DynoFont(name, size).bold();
	}

	public static DynoFont italicFont(String name, int size){
		return new DynoFont(name, size).italic();
	}

	public static DynoFont italicBoldFont(String name, int size){
		return new DynoFont(name, size).italicBold();
	}
}
