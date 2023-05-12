import styled from 'styled-components';

const StyledContainer = styled.div`
  width: 50%;
  display: flex;
  justify-content: space-between;
`;

const StyledMenuItem = styled.div`
  border-radius: 4px;
  min-width: 150px;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-shadow: 0px 2px 1px -1px rgba(0, 0, 0, 0.2), 0px 1px 1px 0px rgba(0, 0, 0, 0.14), 0px 1px 3px 0px rgba(0, 0, 0, 0.12);
`;

export {StyledContainer, StyledMenuItem}