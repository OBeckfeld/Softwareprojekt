package entities;

//interface, damit Objekte der Unterklassen von Entity keinen Zugriff auf den gesamten EntityManager haben
public interface entityRegistry {
    void register(Entity entity);
}
