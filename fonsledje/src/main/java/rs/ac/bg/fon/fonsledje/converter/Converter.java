package rs.ac.bg.fon.fonsledje.converter;

public interface Converter<Dto, Entity> {
    Entity toEntity(Dto dto);
    Dto toDto(Entity entity);
}
