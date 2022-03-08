package token;

import lombok.Data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Data
public class Token {
    public String value;
    public int lineNo;
    public int offset;
    public String type;

    public Token(String value, int lineNo, int offset, String type) {
        this.value = value;
        this.lineNo = lineNo;
        this.offset = offset;
        this.type = type;
    }

    public Token(DataInputStream dataInputStream) throws IOException {
        this.decode(dataInputStream);
    }

    /**
     * encode
     *
     * @param dataOutputStream
     * @throws IOException
     */
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(this.getValue());
        dataOutputStream.writeInt(this.getLineNo());
        dataOutputStream.writeInt(this.getOffset());
        dataOutputStream.writeUTF(this.getType());
    }

    /**
     * decode
     *
     * @param dataInputStream
     * @throws IOException
     */
    public void decode(DataInputStream dataInputStream) throws IOException {
        this.value = dataInputStream.readUTF();
        this.lineNo = dataInputStream.readInt();
        this.offset = dataInputStream.readInt();
        this.type = dataInputStream.readUTF();
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", lineNo=" + lineNo +
                ", offset=" + offset +
                ", type='" + type + '\'' +
                '}';
    }
}
